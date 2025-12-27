import { Product, ProductFormData } from '@/types/product';
import { encodeProducts, decodeProducts, bytesToHex, hexToBytes } from '@/lib/binaryProducts';

const STORAGE_KEY = 'products_hex';

export const productStorage = {
  getAll: (includeInactive = false): Product[] => {
    try {
      const data = localStorage.getItem(STORAGE_KEY);
      if (!data) return [];

      const bytes = hexToBytes(data);
      const decoded = decodeProducts(bytes);

      return includeInactive ? decoded : decoded.filter(p => p.isActive);
    } catch (error) {
      console.error('Error reading products from localStorage:', error);
      return [];
    }
  },

  save: (products: Product[]): void => {
    try {
      const bytes = encodeProducts(products);
      const hex = bytesToHex(bytes);
      localStorage.setItem(STORAGE_KEY, hex);
    } catch (error) {
      console.error('Error saving products to localStorage:', error);
    }
  },

  add: (productData: ProductFormData): Product => {
    const products = productStorage.getAll(true);
    const now = new Date().toISOString();

    const newProduct: Product = {
      ...productData,
      id: crypto.randomUUID(),
      createdAt: now,
      updatedAt: now,
      isActive: true,
    };

    products.push(newProduct);
    productStorage.save(products);
    return newProduct;
  },

  update: (id: string, productData: ProductFormData): Product | null => {
    const products = productStorage.getAll(true);
    const index = products.findIndex(p => p.id === id);
    if (index === -1) return null;

    const updatedProduct: Product = {
      ...products[index],
      ...productData,
      updatedAt: new Date().toISOString(),
    };

    products[index] = updatedProduct;
    productStorage.save(products);
    return updatedProduct;
  },

  delete: (id: string): boolean => {
    const products = productStorage.getAll(true);
    const index = products.findIndex(p => p.id === id);
    if (index === -1) return false;

    products[index].isActive = false; // exclusão lógica
    products[index].updatedAt = new Date().toISOString();
    productStorage.save(products);
    return true;
  },

  search: (query: string, includeInactive = false): Product[] => {
    const products = productStorage.getAll(includeInactive);
    const lowerQuery = query.toLowerCase();

    return products.filter(product =>
      product.name.toLowerCase().includes(lowerQuery) ||
      product.description.toLowerCase().includes(lowerQuery) ||
      product.gtin.includes(query)
    );
  },
};



