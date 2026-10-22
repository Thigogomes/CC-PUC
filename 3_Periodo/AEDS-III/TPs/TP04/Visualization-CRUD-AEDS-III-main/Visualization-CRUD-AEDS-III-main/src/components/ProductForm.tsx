import { useState, useEffect } from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Switch } from '@/components/ui/switch';
import { Product, ProductFormData } from '@/types/product';

interface ProductFormProps {
  product?: Product;
  onSubmit: (data: ProductFormData) => void;
  onCancel: () => void;
}

export const ProductForm = ({ product, onSubmit, onCancel }: ProductFormProps) => {
  const [formData, setFormData] = useState<ProductFormData>({
    name: '',
    description: '',
    price: NaN,
    gtin: '',
    isActive: true,
  });
  const [priceInput, setPriceInput] = useState('');

  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    if (product) {
      setFormData({
        name: product.name,
        description: product.description,
        price: product.price,
        gtin: product.gtin,
        isActive: product.isActive,
      });
      setPriceInput(product.price.toFixed(2));
    }
  }, [product]);

  const validateGTIN13 = (gtin: string): boolean => {
    // GTIN-13 deve ter exatamente 13 dígitos
    return /^\d{13}$/.test(gtin);
  };

  const validateForm = (): boolean => {
    const newErrors: Record<string, string> = {};

    if (!formData.name.trim()) {
      newErrors.name = 'Nome é obrigatório';
    }

    if (!formData.description.trim()) {
      newErrors.description = 'Descrição é obrigatória';
    }

    if (!Number.isFinite(formData.price) || formData.price <= 0) {
      newErrors.price = 'Preço deve ser maior que zero';
    }

    if (!validateGTIN13(formData.gtin)) {
      newErrors.gtin = 'GTIN-13 deve conter exatamente 13 dígitos';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validateForm()) {
      onSubmit(formData);
      resetForm();
    }
  };

  const resetForm = () => {
    setFormData({
      name: '',
      description: '',
      price: NaN,
      gtin: '',
      isActive: true,
    });
    setPriceInput('');
    setErrors({});
  };

  const handlePriceChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const raw = e.target.value;
    if (!/^[0-9]*[.,]?[0-9]*$/.test(raw)) return;
    setPriceInput(raw);
    const parsed = parseFloat(raw.replace(',', '.'));
    setFormData({ ...formData, price: Number.isFinite(parsed) ? parsed : NaN });
  };

  const handlePriceBlur = () => {
    const parsed = parseFloat(priceInput.replace(',', '.'));
    if (Number.isFinite(parsed)) {
      setPriceInput(parsed.toFixed(2));
      setFormData({ ...formData, price: parsed });
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="space-y-2">
        <Label htmlFor="name">Nome do Produto</Label>
        <Input
          id="name"
          value={formData.name}
          onChange={(e) => setFormData({ ...formData, name: e.target.value })}
          placeholder="Digite o nome do produto"
        />
        {errors.name && <p className="text-sm text-destructive">{errors.name}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="description">Descrição</Label>
        <Textarea
          id="description"
          value={formData.description}
          onChange={(e) => setFormData({ ...formData, description: e.target.value })}
          placeholder="Digite a descrição do produto"
          rows={3}
        />
        {errors.description && <p className="text-sm text-destructive">{errors.description}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="price">Preço (R$)</Label>
        <Input
          id="price"
          type="text"
          inputMode="decimal"
          pattern="[0-9]*[.,]?[0-9]*"
          value={priceInput}
          onChange={handlePriceChange}
          onBlur={handlePriceBlur}
          placeholder="0,00"
        />
        {errors.price && <p className="text-sm text-destructive">{errors.price}</p>}
      </div>

      <div className="space-y-2">
        <Label htmlFor="gtin">GTIN-13</Label>
        <Input
          id="gtin"
          value={formData.gtin}
          onChange={(e) => setFormData({ ...formData, gtin: e.target.value.replace(/\D/g, '') })}
          placeholder="1234567890123"
          maxLength={13}
        />
        {errors.gtin && <p className="text-sm text-destructive">{errors.gtin}</p>}
      </div>

      <div className="flex items-center space-x-2">
        <Switch
          id="isActive"
          checked={formData.isActive}
          onCheckedChange={(checked) => setFormData({ ...formData, isActive: checked })}
        />
        <Label htmlFor="isActive">Produto Ativo</Label>
      </div>

      <div className="flex gap-2 justify-end">
        <Button type="button" variant="outline" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit">
          {product ? 'Atualizar' : 'Cadastrar'}
        </Button>
      </div>
    </form>
  );
};
