import React, { useEffect, useState } from 'react';
import { Product } from '@/types/product';
import { productStorage } from '@/lib/productStorage';
import { ProductTable } from './ProductTable';
import { Button } from '@/components/ui/button';

export const ProductManagement: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [showInactive, setShowInactive] = useState(false);

  const loadProducts = () => {
    const prods = productStorage.getAll(true); // pega todos
    setProducts(showInactive ? prods : prods.filter(p => p.isActive));
  };

  useEffect(() => {
    loadProducts();
  }, [showInactive]);

  const handleDelete = (id: string) => {
    productStorage.delete(id); // exclusão lógica
    loadProducts();
  };

  const handleEdit = (product: Product) => {
    console.log('Editar produto:', product);
    // aqui você pode abrir modal ou formulário de edição
  };

  const toggleShowInactive = () => {
    setShowInactive(prev => !prev);
  };

  return (
    <div>
      <Button onClick={toggleShowInactive} className="mb-4">
        {showInactive ? 'Mostrar apenas ativos' : 'Mostrar inativos'}
      </Button>

      <ProductTable
        products={products}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
};
