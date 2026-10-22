import { Product } from '@/types/product';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { Pencil, Trash2 } from 'lucide-react';

interface ProductTableProps {
  products: Product[];
  onEdit: (product: Product) => void;
  onDelete: (id: string) => void;
}

export const ProductTable = ({ products, onEdit, onDelete }: ProductTableProps) => {
  const formatPrice = (price: number) =>
    new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(price);

  const formatDate = (dateString: string) =>
    new Date(dateString).toLocaleDateString('pt-BR');

  if (products.length === 0) {
    return (
      <div className="text-center py-12">
        <p className="text-muted-foreground">Nenhum produto cadastrado</p>
      </div>
    );
  }

  return (
    <div className="border rounded-lg">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Nome</TableHead>
            <TableHead>Descrição</TableHead>
            <TableHead>Preço</TableHead>
            <TableHead>GTIN-13</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Atualizado em</TableHead>
            <TableHead className="text-right">Ações</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.id}>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900 font-medium' : 'font-medium'}>
                {product.name}
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900' : ''}>
                {product.description}
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900' : ''}>
                {formatPrice(product.price)}
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900 font-mono' : 'font-mono text-sm'}>
                {product.gtin}
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900' : ''}>
                <Badge variant={product.isActive ? 'default' : 'secondary'}>
                  {product.isActive ? 'Ativo' : 'Inativo'}
                </Badge>
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900' : ''}>
                {formatDate(product.updatedAt)}
              </TableCell>
              <TableCell className={!product.isActive ? 'bg-yellow-100 text-yellow-900 text-right' : 'text-right'}>
                <div className="flex gap-2 justify-end">
                  <Button
                    variant="outline"
                    size="icon"
                    onClick={() => onEdit(product)}
                    disabled={!product.isActive}
                  >
                    <Pencil className="h-4 w-4" />
                  </Button>
                  <Button
                    variant="destructive"
                    size="icon"
                    onClick={() => onDelete(product.id)}
                  >
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};
