import { useEffect, useState } from 'react';
import { HexViewer } from '@/components/HexViewer';
import { ProductForm } from '@/components/ProductForm';
import { ProductTable } from '@/components/ProductTable';
import { SiteFooter } from '@/components/SiteFooter';
import { SiteHeader } from '@/components/SiteHeader';
import { productStorage } from '@/lib/productStorage';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { Button } from '@/components/ui/button';
import { Product, ProductFormData } from '@/types/product';
import { Package, Plus, Search } from 'lucide-react';
import { toast } from 'sonner';

type LastOperation = {
  type: 'insert' | 'update' | 'delete';
  productId: string;
};

const Index = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | undefined>();
  const [lastOperation, setLastOperation] = useState<LastOperation | undefined>();

  useEffect(() => {
    loadProducts();
  }, []);

  useEffect(() => {
    if (searchQuery.trim()) {
      setFilteredProducts(productStorage.search(searchQuery, false)); // só ativos
    } else {
      setFilteredProducts(products);
    }
  }, [searchQuery, products]);

  const loadProducts = () => {
    const loadedProducts = productStorage.getAll(false); // só ativos
    setProducts(loadedProducts);
    setFilteredProducts(loadedProducts);
  };

  const handleSubmit = (data: ProductFormData) => {
    if (editingProduct) {
      const updated = productStorage.update(editingProduct.id, data);
      if (updated) {
        setLastOperation({ type: 'update', productId: updated.id });
        toast.success('Produto atualizado com sucesso!');
        loadProducts();
      }
    } else {
      const newProduct = productStorage.add(data);
      setLastOperation({ type: 'insert', productId: newProduct.id });
      toast.success('Produto cadastrado com sucesso!');
      loadProducts();
    }
    setIsDialogOpen(false);
    setEditingProduct(undefined);
  };

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setIsDialogOpen(true);
  };

  const handleDelete = (id: string) => {
    if (window.confirm('Tem certeza que deseja excluir este produto?')) {
      const deleted = productStorage.delete(id);
      if (deleted) {
        setLastOperation({ type: 'delete', productId: id });
        toast.success('Produto excluído com sucesso!');
        loadProducts();
      }
    }
  };

  const handleNewProduct = () => {
    setEditingProduct(undefined);
    setIsDialogOpen(true);
  };

  const handleCancel = () => {
    setIsDialogOpen(false);
    setEditingProduct(undefined);
  };

  return (
    <div className="min-h-screen bg-background text-foreground flex flex-col">
      <SiteHeader onNewProduct={handleNewProduct} totalProducts={products.length} />
      <main className="flex-1">
        <section id="produtos" className="container px-4 pb-12 pt-10">
          {/* ADICIONADO sm:px-6 PARA ALINHAR COM O CARDHEADER */}
          <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between sm:px-6">
            <div className="flex items-center gap-3">
              <div className="grid h-12 w-12 place-items-center rounded-2xl bg-primary/10 text-primary">
                <Package className="h-6 w-6" />
              </div>
              <div>
                <h2 className="text-3xl font-semibold leading-tight">Produtos</h2>
                <p className="text-sm text-muted-foreground">
                  Cadastre, edite e gerencie seus produtos de forma simples e eficiente.
                </p>
              </div>
            </div>

            {/* VERSÃO ESQUERDA */}
            <div className="flex w-full sm:w-auto justify-start pl-0 sm:pl-0">
                <Button
                  variant="secondary"
                  asChild
                  className="w-fit bg-[hsl(210_30%_94%)] text-primary hover:bg-[hsl(210_30%_92%)]"
                >
                  <a href="#dados">Ver Hex Viewer</a>
                </Button>
              </div>
            </div>

          <Card className="border border-border/70 shadow-[0_24px_60px_hsl(0_0%_0%_/_06%)]">
            <CardHeader className="border-b border-border/60">
              <div className="flex flex-col gap-4">
                <div className="flex flex-col gap-3 md:flex-row md:items-center md:justify-between">
                  <div>
                    <CardTitle>Produtos cadastrados</CardTitle>
                    <CardDescription className="my-[5px]">Total: {products.length} produto(s)</CardDescription>
                  </div>
                  <Button onClick={handleNewProduct} className="w-full md:w-auto shadow-sm shadow-primary/20">
                    <Plus className="mr-2 h-4 w-4" />
                    Novo Produto
                  </Button>
                </div>
                <div className="relative w-full md:max-w-md">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
                  <Input
                    placeholder="Buscar por nome, descrição ou GTIN..."
                    value={searchQuery}
                    onChange={e => setSearchQuery(e.target.value)}
                    className="pl-10"
                  />
                </div>
              </div>
            </CardHeader>
            <CardContent className="pt-6">
              <ProductTable products={filteredProducts} onEdit={handleEdit} onDelete={handleDelete} />
            </CardContent>
          </Card>
        </section>

        <section id="dados" className="container px-4 pb-16">
          {/* HexViewer sempre mostra todos os produtos, inclusive inativos */}
          <HexViewer
            lastOperation={lastOperation}
            onClear={() => {
              loadProducts();
              setLastOperation(undefined);
            }}
          />
        </section>
      </main>

      <SiteFooter onNewProduct={handleNewProduct} totalProducts={products.length} />

      <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>{editingProduct ? 'Editar Produto' : 'Cadastrar Novo Produto'}</DialogTitle>
            <DialogDescription>
              {editingProduct ? 'Atualize as informações do produto abaixo' : 'Preencha os dados do novo produto'}
            </DialogDescription>
          </DialogHeader>
          <ProductForm product={editingProduct} onSubmit={handleSubmit} onCancel={handleCancel} />
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Index;
