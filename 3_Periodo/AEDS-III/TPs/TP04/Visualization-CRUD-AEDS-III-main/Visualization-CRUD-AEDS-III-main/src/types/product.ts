export interface Product {
  id: string;
  name: string;
  description: string;
  price: number;
  gtin: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export type ProductFormData = Omit<Product, 'id' | 'createdAt' | 'updatedAt'>;
