import { useEffect, useMemo, useRef, useState } from 'react';
import { FileCode, Copy, Eye, EyeOff, Sparkles, Filter, Scan, X, GitCompare, Bookmark, Play, Camera, Trash2 } from 'lucide-react';
import clsx from 'clsx';

import { productStorage } from '@/lib/productStorage';
import { encodeProducts } from '@/lib/binaryProducts';
import type { Product } from '@/types/product';

import { Badge } from '@/components/ui/badge';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover';
import { ScrollArea } from '@/components/ui/scroll-area';
import { Separator } from '@/components/ui/separator';
import { Switch } from '@/components/ui/switch';
import { ToggleGroup, ToggleGroupItem } from '@/components/ui/toggle-group';
import { Tooltip, TooltipContent, TooltipTrigger } from '@/components/ui/tooltip';

type FieldKey =
  | 'id'
  | 'name'
  | 'description'
  | 'price'
  | 'gtin'
  | 'isActive'
  | 'createdAt'
  | 'updatedAt';

type HexByte = {
  hex: string;
  ascii: string;
  highlight?: 'insert' | 'update' | 'delete'; // verde, laranja, vermelho
  productId: string;
  productName: string;
  field: FieldKey;
};

type ProductSection = {
  field: FieldKey;
  label: string;
  start: number; // index in the global hex array
  end: number;
};

type ProductSlice = {
  productId: string;
  productName: string;
  start: number;
  end: number;
  sections: ProductSection[];
  highlight?: 'insert' | 'update' | 'delete';
  isActive: boolean;
};

const textEncoder = new TextEncoder();

const fieldLabels: Record<FieldKey, string> = {
  id: 'ID (UUID)',
  name: 'Nome',
  description: 'Descricao',
  price: 'Preco (float64 LE)',
  gtin: 'GTIN',
  isActive: 'Ativo (1/0)',
  createdAt: 'Criado em',
  updatedAt: 'Atualizado em',
};

const encodeStringLocal = (value: string) => {
  const bytes = textEncoder.encode(value);
  const buf = new Uint8Array(2 + bytes.length);
  const view = new DataView(buf.buffer);
  view.setUint16(0, bytes.length, true);
  buf.set(bytes, 2);
  return buf;
};

const encodeFloat64Local = (value: number) => {
  const buf = new ArrayBuffer(8);
  const view = new DataView(buf);
  view.setFloat64(0, value, true);
  return new Uint8Array(buf);
};

const encodeBooleanLocal = (value: boolean) => new Uint8Array([value ? 1 : 0]);

const palette = (colorBlind: boolean) => ({
  insert: colorBlind ? 'bg-blue-100 text-blue-800' : 'bg-emerald-100 text-emerald-700',
  update: colorBlind ? 'bg-purple-100 text-purple-800' : 'bg-amber-100 text-amber-700',
  delete: colorBlind ? 'bg-slate-800 text-white' : 'bg-red-100 text-red-700',
  insertText: colorBlind ? 'text-blue-800' : 'text-emerald-700',
  updateText: colorBlind ? 'text-purple-800' : 'text-amber-700',
  deleteText: colorBlind ? 'text-slate-900' : 'text-red-700',
  minimap: {
    insert: colorBlind ? 'bg-blue-500/80' : 'bg-emerald-400/80',
    update: colorBlind ? 'bg-purple-500/80' : 'bg-amber-400/80',
    delete: colorBlind ? 'bg-slate-800/80' : 'bg-red-400/80',
    neutral: 'bg-slate-500/50',
  },
});

const hexClass = (highlight: HexByte['highlight'], colorBlind: boolean) =>
  clsx(
    highlight === 'delete'
      ? palette(colorBlind).delete
      : highlight === 'insert'
        ? palette(colorBlind).insert
        : highlight === 'update'
          ? palette(colorBlind).update
          : 'text-slate-900',
  );

const asciiClass = (highlight: HexByte['highlight'], colorBlind: boolean) =>
  clsx(
    highlight === 'delete'
      ? palette(colorBlind).deleteText
      : highlight === 'insert'
        ? palette(colorBlind).insertText
        : highlight === 'update'
          ? palette(colorBlind).updateText
          : 'text-slate-800',
  );

const isEditableElement = (el: Element | null) => {
  if (!el) return false;
  const target = el as HTMLElement;
  if (target.isContentEditable) return true;
  if (target.tagName === 'TEXTAREA' || target.tagName === 'SELECT') return true;
  if (target.tagName === 'INPUT') {
    const type = (target as HTMLInputElement).type;
    return ['text', 'search', 'email', 'url', 'tel', 'password', 'number'].includes(type) || type === '';
  }
  return false;
};

export const HexViewer = ({
  lastOperation,
  onClear,
}: {
  lastOperation?: { type: 'insert' | 'update' | 'delete'; productId: string };
  onClear?: () => void;
}) => {
  const [hexData, setHexData] = useState<HexByte[]>([]);
  const [productSlices, setProductSlices] = useState<ProductSlice[]>([]);
  const [bytesPerLine, setBytesPerLine] = useState(16);
  const [showAscii, setShowAscii] = useState(true);
  const [highlightFilter, setHighlightFilter] = useState<'all' | 'insert' | 'update' | 'delete'>('all');
  const [copied, setCopied] = useState<'hex' | 'ascii' | null>(null);
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);
  const [zoomVisible, setZoomVisible] = useState(false);
  const zoomOpen = !!selectedProductId && zoomVisible;
  const [pinOffset, setPinOffset] = useState<number | null>(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [jumpOffset, setJumpOffset] = useState('');
  const [bookmarks, setBookmarks] = useState<{ offset: number; label: string }[]>([]);
  const [bookmarkLabel, setBookmarkLabel] = useState('');
  const [secondaryProductId, setSecondaryProductId] = useState<string | null>(null);
  const [recentOps, setRecentOps] = useState<{ type: string; productId: string; ts: string }[]>([]);
  const [colorBlindMode, setColorBlindMode] = useState(false);
  const [hoveredIndex, setHoveredIndex] = useState<number | null>(null);
  const [zoomHoverIndex, setZoomHoverIndex] = useState<number | null>(null);
  const [prefsLoaded, setPrefsLoaded] = useState(false);
  const [showZoomHint, setShowZoomHint] = useState(false);
  const [hintClosing, setHintClosing] = useState(false);

  const scrollRef = useRef<HTMLDivElement>(null);
  const searchRef = useRef<HTMLInputElement>(null);
  const jumpRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const products = productStorage.getAll(true);
    const converted = convertProductsToHex(products, lastOperation);
    setHexData(converted.bytes);
    setProductSlices(converted.slices);

    if (selectedProductId && !converted.slices.find(s => s.productId === selectedProductId)) {
      setSelectedProductId(null);
      setZoomVisible(false);
    }
    if (lastOperation) {
      setRecentOps(prev => [
        { type: lastOperation.type, productId: lastOperation.productId, ts: new Date().toLocaleTimeString() },
        ...prev,
      ].slice(0, 5));
    }
  }, [lastOperation, selectedProductId]);

  // carregar preferencias
  useEffect(() => {
    const stored = localStorage.getItem('hexPrefs');
    if (stored) {
      try {
        const parsed = JSON.parse(stored);
        if (parsed.bytesPerLine) setBytesPerLine(parsed.bytesPerLine);
        if (typeof parsed.showAscii === 'boolean') setShowAscii(parsed.showAscii);
        if (parsed.highlightFilter) setHighlightFilter(parsed.highlightFilter);
        if (typeof parsed.colorBlindMode === 'boolean') setColorBlindMode(parsed.colorBlindMode);
      } catch (e) {
        console.warn('prefs parse error', e);
      }
    }
    setPrefsLoaded(true);
  }, []);

  useEffect(() => {
    const seen = localStorage.getItem('hexZoomHintSeen');
    setShowZoomHint(!seen);
  }, []);

  useEffect(() => {
    if (!prefsLoaded) return;
    localStorage.setItem(
      'hexPrefs',
      JSON.stringify({ bytesPerLine, showAscii, highlightFilter, colorBlindMode }),
    );
  }, [bytesPerLine, showAscii, highlightFilter, colorBlindMode, prefsLoaded]);

  const stats = useMemo(
    () =>
      hexData.reduce(
        (acc, byte) => {
          acc.total += 1;
          if (byte.highlight === 'insert') acc.insert += 1;
          if (byte.highlight === 'update') acc.update += 1;
          if (byte.highlight === 'delete') acc.delete += 1;
          return acc;
        },
        { total: 0, insert: 0, update: 0, delete: 0 },
      ),
    [hexData],
  );

  const selectedSlice = useMemo(
    () => (selectedProductId ? productSlices.find(p => p.productId === selectedProductId) || null : null),
    [productSlices, selectedProductId],
  );

  const secondarySlice = useMemo(
    () => (secondaryProductId ? productSlices.find(p => p.productId === secondaryProductId) || null : null),
    [productSlices, secondaryProductId],
  );

  useEffect(() => {
    const handler = (e: KeyboardEvent) => {
      const activeElement = document.activeElement;
      if (isEditableElement(activeElement)) return;

      if (e.key === 'Escape') {
        setSelectedProductId(null);
        setZoomVisible(false);
      }
      if (e.key.toLowerCase() === 'z') {
        setZoomVisible(v => !v);
      }
      if (e.key.toLowerCase() === 'f') {
        searchRef.current?.focus();
      }
      if (e.key.toLowerCase() === 'g') {
        jumpRef.current?.focus();
      }
      if (e.key.toLowerCase() === 'p' && pinOffset !== null) {
        setPinOffset(null);
      }
      if ((e.key === 'ArrowDown' || e.key === 'ArrowUp') && productSlices.length > 0) {
        e.preventDefault();
        const idx = productSlices.findIndex(p => p.productId === selectedProductId);
        if (idx === -1 && productSlices.length) {
          setSelectedProductId(productSlices[0].productId);
          setZoomVisible(true);
        } else {
          const nextIdx = e.key === 'ArrowDown' ? idx + 1 : idx - 1;
          const bounded = (nextIdx + productSlices.length) % productSlices.length;
          setSelectedProductId(productSlices[bounded].productId);
          setZoomVisible(true);
          scrollToOffset(productSlices[bounded].start);
        }
      }
    };
    window.addEventListener('keydown', handler);
    return () => window.removeEventListener('keydown', handler);
  }, [productSlices, selectedProductId]);

  const scrollToOffset = (offset: number) => {
    if (!scrollRef.current) return;
    const line = Math.floor(offset / bytesPerLine);
    const target = scrollRef.current.querySelector<HTMLElement>(`[data-offset-line="${line}"]`);
    if (target) {
      target.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
  };

  const handleCopy = async (type: 'hex' | 'ascii' | 'zoom' | string) => {
    const text =
      type === 'hex'
        ? hexData.map(b => b.hex).join(' ')
        : type === 'ascii'
          ? hexData.map(b => b.ascii).join('')
          : type === 'zoom'
            ? selectedSlice
              ? selectedSlice.sections
                  .map(section => {
                    const bytes = hexData.slice(section.start, section.end);
                    return `${fieldLabels[section.field]}: ${bytes.map(b => b.hex).join(' ')} | ASCII: ${bytes
                      .map(b => b.ascii)
                      .join('')}`;
                  })
                  .join('\n')
              : ''
            : type.startsWith('line-')
              ? (() => {
                  const start = Number(type.replace('line-', ''));
                  const lineBytes = hexData.slice(start, start + bytesPerLine);
                  return `${start.toString(16).padStart(8, '0')}: ${lineBytes
                    .map(b => b.hex)
                    .join(' ')} | ASCII: ${lineBytes.map(b => b.ascii).join('')}`;
                })()
              : '';
    try {
      await navigator.clipboard.writeText(text);
      setCopied(type === 'zoom' ? 'hex' : (type === 'ascii' || type === 'hex' ? type : 'hex'));
      setTimeout(() => setCopied(null), 1500);
    } catch (err) {
      console.error('Clipboard error', err);
    }
  };

  const formatHexLine = (startIndex: number, endIndex: number) => {
    const lineData = hexData.slice(startIndex, endIndex);
    const offset = startIndex.toString(16).padStart(8, '0').toUpperCase();
    const matchesSearch = searchTerm
      ? lineData.some(
          b =>
            b.hex.toLowerCase().includes(searchTerm.toLowerCase()) ||
            (b.ascii !== '.' && b.ascii.toLowerCase().includes(searchTerm.toLowerCase())),
        )
      : true;

    return (
      <div
        key={startIndex}
        data-offset-line={startIndex / bytesPerLine}
        className={clsx(
          'group relative flex items-center gap-4 rounded-md px-3 py-1 font-mono text-xs transition-colors',
          pinOffset === startIndex ? 'bg-emerald-50/80 shadow-inner' : 'hover:bg-white/70',
          !matchesSearch && 'opacity-30',
        )}
      >
        <div className="flex items-center gap-1 w-24">
          <span
            className="w-16 cursor-pointer font-sans text-[11px] text-muted-foreground"
            onClick={() => setPinOffset(pinOffset === startIndex ? null : startIndex)}
            title="Fixar linha"
          >
            {offset}
          </span>
          <button
            className="text-slate-400 hover:text-slate-600"
            title="Copiar linha (HEX + ASCII)"
            onClick={() => handleCopy('line-' + startIndex)}
          >
            <Copy className="h-3 w-3" />
          </button>
        </div>

        <div className="flex flex-1 flex-wrap gap-1">
          {lineData.map((byte, idx) => {
            const isDimmed = highlightFilter !== 'all' && byte.highlight !== highlightFilter;
            const isSelected = selectedProductId === byte.productId;
            return (
              <span
                key={idx}
                onClick={() => {
                  if (showZoomHint) {
                    dismissZoomHint();
                  }
                  const id = byte.productId;
                  setSelectedProductId(prev => {
                    if (prev === id) {
                      setZoomVisible(false);
                      return null;
                    }
                    setZoomVisible(true);
                    return id;
                  });
                }}
                title={`${byte.productName} - ${fieldLabels[byte.field]}`}
                onMouseEnter={() => setHoveredIndex(startIndex + idx)}
                onMouseLeave={() => setHoveredIndex(null)}
                className={clsx(
                  'cursor-pointer rounded px-1 transition-all duration-150',
                  hexClass(byte.highlight, colorBlindMode),
                  isDimmed && 'opacity-40',
                  isSelected && 'ring-2 ring-emerald-400 ring-offset-1 bg-emerald-50 shadow-sm scale-105',
                  (byte.highlight === 'insert' || byte.highlight === 'update') && 'animate-pulse-soft',
                )}
              >
                {byte.hex}
              </span>
            );
          })}
          {Array(bytesPerLine - lineData.length)
            .fill(null)
            .map((_, idx) => (
              <span key={`empty-${idx}`} className="text-transparent">
                00
              </span>
            ))}
        </div>

        {showAscii && (
          <div className="ml-auto flex items-center gap-2 rounded-full border border-slate-200/70 bg-white/85 px-3 py-1 text-[11px] font-mono shadow-sm">
            <span className="text-[10px] uppercase tracking-wide text-slate-400">ASCII</span>
            <div className="flex gap-[3px]">
              {lineData.map((byte, idx) => {
                const isDimmed = highlightFilter !== 'all' && byte.highlight !== highlightFilter;
                const isHovered = hoveredIndex === startIndex + idx;
                return (
                  <span
                    key={idx}
                    className={clsx(
                      'rounded px-[4px] leading-4 transition-colors',
                      asciiClass(byte.highlight, colorBlindMode),
                      isDimmed && 'opacity-40',
                      isHovered && 'ring-2 ring-indigo-400 ring-offset-1 bg-indigo-50',
                    )}
                    title={byte.ascii === '.' ? 'Nao imprimivel' : `ASCII: ${byte.ascii}`}
                  >
                    {byte.ascii}
                  </span>
                );
              })}
            </div>
          </div>
        )}
      </div>
    );
  };

  const lines = [];
  for (let i = 0; i < hexData.length; i += bytesPerLine) {
    lines.push(formatHexLine(i, Math.min(i + bytesPerLine, hexData.length)));
  }
  const lineCount = Math.ceil(hexData.length / bytesPerLine);
  const miniMap = useMemo(() => {
    const arr: { line: number; weight: number; highlight?: HexByte['highlight'] }[] = [];
    for (let line = 0; line < lineCount; line++) {
      const slice = hexData.slice(line * bytesPerLine, (line + 1) * bytesPerLine);
      const weight = slice.filter(b => b.highlight).length;
      const highlight = slice.find(b => b.highlight)?.highlight;
      arr.push({ line, weight, highlight });
    }
    return arr;
  }, [hexData, lineCount, bytesPerLine]);

  const dismissZoomHint = () => {
    if (hintClosing || (!showZoomHint && !hintClosing)) return;
    setHintClosing(true);
    setTimeout(() => {
      setShowZoomHint(false);
      setHintClosing(false);
      localStorage.setItem('hexZoomHintSeen', 'true');
    }, 220);
  };

  const handleClear = () => {
    productStorage.save([]);
    setHexData([]);
    setProductSlices([]);
    setSelectedProductId(null);
    setSecondaryProductId(null);
    setZoomVisible(false);
    setPinOffset(null);
    setBookmarks([]);
    setRecentOps([]);
    setSearchTerm('');
    setJumpOffset('');
    onClear?.();
  };

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center gap-2">
          <FileCode className="h-5 w-5 text-primary" />
          <CardTitle>Visualização Hexadecimal</CardTitle>
        </div>
        <CardDescription>
          Produtos inativos: <span className={palette(colorBlindMode).deleteText}>Vermelho</span>. Inseridos:{' '}
          <span className={palette(colorBlindMode).insertText}>Verde</span>. Atualizados:{' '} 
          <span className={palette(colorBlindMode).updateText}>Laranja</span>. 
        </CardDescription>
      </CardHeader>
      <CardContent>
        {(showZoomHint || hintClosing) && (
          <div
            className={clsx(
              'mb-3 flex flex-wrap items-center justify-between gap-3 rounded-md border border-emerald-200 bg-emerald-50 px-3 py-2 text-sm text-emerald-900 shadow-sm',
              hintClosing ? 'animate-zoom-hint-exit' : 'animate-zoom-hint',
            )}
          >
            <div className="flex items-center gap-2">
              <Scan className="h-4 w-4" />
              <span>Clique em qualquer byte para abrir o zoom da entidade. Use Z para alternar.</span>
            </div>
            <Button
              size="sm"
              variant="ghost"
              className="h-8 px-3 text-emerald-900 hover:bg-emerald-100"
              onClick={dismissZoomHint}
            >
              Entendi
            </Button>
          </div>
        )}

        <div className="mb-4 flex flex-wrap items-center gap-3 rounded-lg bg-gradient-to-r from-slate-50 via-white to-emerald-50/40 p-4">
          <div className="flex items-center gap-2">
            <Badge variant="outline">Bytes: {stats.total}</Badge>
            <Badge className={palette(colorBlindMode).insert}>+{stats.insert || 0}</Badge>
            <Badge className={palette(colorBlindMode).update}>~{stats.update || 0}</Badge>
            <Badge className={palette(colorBlindMode).delete}>-{stats.delete || 0}</Badge>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Sparkles className="h-4 w-4 text-emerald-600" />
            <span className="text-sm text-muted-foreground">Modo de destaque</span>
            <ToggleGroup
              type="single"
              value={highlightFilter}
              onValueChange={value => value && setHighlightFilter(value as typeof highlightFilter)}
              className="rounded-md bg-white/70 p-1 shadow-inner"
            >
              <ToggleGroupItem value="all" className="px-2 text-xs">
                Tudo
              </ToggleGroupItem>
              <ToggleGroupItem value="insert" className="px-2 text-xs">
                Inseridos
              </ToggleGroupItem>
              <ToggleGroupItem value="update" className="px-2 text-xs">
                Atualizados
              </ToggleGroupItem>
              <ToggleGroupItem value="delete" className="px-2 text-xs">
                Inativos
              </ToggleGroupItem>
            </ToggleGroup>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Filter className="h-4 w-4 text-slate-600" />
            <span className="text-sm text-muted-foreground">Bytes/linha</span>
            <ToggleGroup
              type="single"
              value={bytesPerLine.toString()}
              onValueChange={value => value && setBytesPerLine(Number(value))}
              className="rounded-md bg-white/70 p-1 shadow-inner"
            >
              <ToggleGroupItem value="8" className="px-2 text-xs">
                8
              </ToggleGroupItem>
              <ToggleGroupItem value="16" className="px-2 text-xs">
                16
              </ToggleGroupItem>
            </ToggleGroup>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Switch id="ascii-toggle" checked={showAscii} onCheckedChange={setShowAscii} />
            <label htmlFor="ascii-toggle" className="flex items-center gap-1 text-sm text-muted-foreground">
              {showAscii ? <Eye className="h-4 w-4" /> : <EyeOff className="h-4 w-4" />}
              ASCII
            </label>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Switch id="cb-toggle" checked={colorBlindMode} onCheckedChange={setColorBlindMode} />
            <label htmlFor="cb-toggle" className="flex items-center gap-1 text-sm text-muted-foreground">
              Daltonismo
            </label>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Tooltip>
              <TooltipTrigger asChild>
                <Button variant="outline" size="sm" onClick={() => handleCopy('hex')}>
                  <Copy className="mr-2 h-4 w-4" />
                  Copiar HEX
                </Button>
              </TooltipTrigger>
              <TooltipContent>Copiar apenas os bytes</TooltipContent>
            </Tooltip>
            <Tooltip>
              <TooltipTrigger asChild>
                <Button variant="outline" size="sm" onClick={() => handleCopy('ascii')}>
                  <Copy className="mr-2 h-4 w-4" />
                  Copiar ASCII
                </Button>
              </TooltipTrigger>
              <TooltipContent>Copiar representacao ASCII</TooltipContent>
            </Tooltip>
            {copied && <span className="text-sm text-emerald-600">Copiado!</span>}
          </div>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex items-center gap-2">
            <Tooltip>
              <TooltipTrigger asChild>
                <Button variant="destructive" size="sm" onClick={handleClear}>
                  <Trash2 className="mr-2 h-4 w-4" />
                  Limpar Produtos
                </Button>
              </TooltipTrigger>
              <TooltipContent>Remove os produtos armazenados e limpa a visualização.</TooltipContent>
            </Tooltip>
          </div>

          <Separator orientation="vertical" className="h-6" />

          <Popover>
            <PopoverTrigger asChild>
              <Button variant="outline" size="sm">Atalhos</Button>
            </PopoverTrigger>
            <PopoverContent className="w-64 space-y-2 text-sm" align="start">
              <p className="text-xs uppercase tracking-wide text-slate-500">Atalhos do viewer</p>
              <div className="grid grid-cols-1 gap-1">
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">F</span>
                  <span>Focar busca</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">G</span>
                  <span>Focar ir para offset</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">Z</span>
                  <span>Mostrar/ocultar zoom</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">Esc</span>
                  <span>Sair do zoom</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">P</span>
                  <span>Remover pin fixado</span>
                </div>
                <div className="flex items-center justify-between">
                  <span className="font-mono text-xs">↑ / ↓</span>
                  <span>Trocar entidade focada</span>
                </div>
              </div>
            </PopoverContent>
          </Popover>

          <Separator orientation="vertical" className="h-6" />

          <div className="flex flex-wrap items-center gap-2">
            <input
              value={searchTerm}
              onChange={e => setSearchTerm(e.target.value)}
              placeholder="Buscar ASCII/HEX"
              ref={searchRef}
              className="h-9 rounded-md border px-2 text-sm shadow-inner"
            />
            <input
              value={jumpOffset}
              onChange={e => setJumpOffset(e.target.value)}
              placeholder="Ir para 0x..."
              ref={jumpRef}
              className="h-9 w-28 rounded-md border px-2 text-sm shadow-inner"
            />
            <Button
              size="sm"
              variant="secondary"
              onClick={() => {
                const value = jumpOffset.trim().replace(/^0x/i, '');
                const num = Number.parseInt(value || '0', 16);
                if (!Number.isNaN(num)) {
                  scrollToOffset(num);
                  setPinOffset(Math.floor(num / bytesPerLine) * bytesPerLine);
                }
              }}
            >
              Ir
            </Button>
          </div>

          {selectedSlice && (
            <div className="ml-auto flex flex-wrap items-center gap-2">
              <Badge variant="outline" className="hidden sm:inline-flex">
                Foco: {selectedSlice.productName || 'Produto'}
              </Badge>
              <Button
                variant="secondary"
                size="sm"
                onClick={() => {
                  setSelectedProductId(null);
                  setZoomVisible(false);
                }}
              >
                Sair do zoom
              </Button>
              <div className="flex items-center gap-2 rounded-md border bg-white/70 px-2 py-1">
                <GitCompare className="h-4 w-4 text-slate-600" />
                <select
                  className="h-9 rounded-md border px-2 text-sm"
                  value={secondaryProductId || ''}
                  onChange={e => setSecondaryProductId(e.target.value || null)}
                >
                  <option value="">Comparar...</option>
                  {productSlices
                    .filter(p => p.productId !== selectedSlice.productId)
                    .map(p => (
                      <option key={p.productId} value={p.productId}>
                        {p.productName}
                      </option>
                    ))}
                </select>
                {secondaryProductId && (
                  <Button variant="ghost" size="icon" className="h-8 w-8" onClick={() => setSecondaryProductId(null)}>
                    <X className="h-4 w-4" />
                  </Button>
                )}
              </div>
            </div>
          )}
        </div>

        <ScrollArea
          ref={scrollRef}
          className="relative h-[520px] w-full rounded-lg border bg-gradient-to-br from-slate-50 via-white to-emerald-50/30 p-4 shadow-inner"
        >
          <div className="pointer-events-none absolute inset-0 rounded-lg border border-dashed border-muted/50" />
          <div className={clsx('space-y-0.5 transition-all', zoomOpen ? 'pr-[320px]' : 'pr-4')}>
            {lines.length > 0 ? (
              lines
            ) : (
              <div className="py-8 text-center text-muted-foreground">Nenhum dado no armazenamento</div>
            )}
          </div>

          <div className="pointer-events-auto absolute right-1 top-4 bottom-4 w-2 rounded-full bg-slate-200/60">
            {miniMap.map(seg => {
              const paletteMini = palette(colorBlindMode).minimap;
              const color =
                seg.highlight === 'delete'
                  ? paletteMini.delete
                  : seg.highlight === 'insert'
                    ? paletteMini.insert
                    : seg.highlight === 'update'
                      ? paletteMini.update
                      : paletteMini.neutral;
              return (
                <div
                  key={seg.line}
                  className={clsx('mx-auto mt-[1px] w-1 cursor-pointer rounded-sm transition-all hover:w-2', color)}
                  style={{
                    height: '6px',
                    opacity: Math.max(0.25, Math.min(1, seg.weight / bytesPerLine + 0.2)),
                  }}
                  onClick={() => scrollToOffset(seg.line * bytesPerLine)}
                  title={`Offset 0x${(seg.line * bytesPerLine).toString(16).padStart(8, '0')}`}
                />
              );
            })}
          </div>

          {selectedSlice && (
            <div
              className={clsx(
                'pointer-events-auto absolute right-3 top-3 bottom-3 w-[320px] max-w-[42vw] overflow-y-auto rounded-xl border bg-white/95 p-4 shadow-2xl backdrop-blur-md transition-all duration-300',
                zoomVisible ? 'opacity-100 translate-y-0' : 'pointer-events-none opacity-0 translate-y-1',
              )}
            >
              <div className="mb-3 flex flex-wrap items-center gap-2">
                <Scan className="h-5 w-5 text-primary" />
                <p className="text-sm font-semibold text-foreground">Zoom da entidade</p>
                <Badge variant="outline">{selectedSlice.productName || 'Produto'}</Badge>
                <Badge className="bg-slate-900 text-white">ID: {selectedSlice.productId.slice(0, 8)}...</Badge>
                <Badge className={selectedSlice.isActive ? palette(colorBlindMode).insert : palette(colorBlindMode).delete}>
                  {selectedSlice.isActive ? 'Ativo' : 'Inativo'}
                </Badge>
                <Button
                  size="icon"
                  variant="ghost"
                  className="ml-auto h-8 w-8"
                  onClick={() => {
                    setSelectedProductId(null);
                    setZoomVisible(false);
                  }}
                >
                  <X className="h-4 w-4" />
                </Button>
              </div>

              <div className="mb-3 grid gap-2">
                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                  <Play className="h-4 w-4 text-emerald-600" />
                  Ultimas operacoes
                </div>
                <div className="flex flex-wrap gap-1">
                  {recentOps.map(op => (
                    <Badge key={`${op.type}-${op.productId}-${op.ts}`} variant="outline" className="text-[11px]">
                      {op.ts} - {op.type} - {op.productId.slice(0, 6)}
                    </Badge>
                  ))}
                </div>
                <div className="flex items-center gap-2 text-xs text-muted-foreground">
                  <Bookmark className="h-4 w-4 text-amber-600" />
                  Marcadores
                </div>
                <div className="flex flex-wrap items-center gap-2">
                  <input
                    value={bookmarkLabel}
                    onChange={e => setBookmarkLabel(e.target.value)}
                    placeholder="Nome do marcador"
                    className="h-8 rounded-md border px-2 text-xs shadow-inner"
                  />
                  <Button
                    size="sm"
                    variant="secondary"
                    className="text-[11px]"
                    onClick={() => {
                      if (pinOffset === null && selectedSlice) {
                        setPinOffset(selectedSlice.start);
                      }
                      const offset = pinOffset ?? selectedSlice?.start ?? 0;
                      const label = bookmarkLabel.trim() || `Offset ${offset.toString(16).padStart(6, '0')}`;
                      setBookmarks(prev => {
                        const exists = prev.find(b => b.offset === offset && b.label === label);
                        if (exists) return prev;
                        return [...prev, { offset, label }];
                      });
                      setBookmarkLabel('');
                    }}
                  >
                    Salvar
                  </Button>
                </div>
                <div className="flex flex-wrap gap-1">
                  {bookmarks.map(b => (
                    <div key={`${b.offset}-${b.label}`} className="flex items-center gap-1">
                      <Button
                        variant="outline"
                        size="sm"
                        className="text-[11px]"
                        onClick={() => {
                          scrollToOffset(b.offset);
                          setPinOffset(b.offset);
                        }}
                      >
                        {b.label} @0x{b.offset.toString(16).padStart(6, '0')}
                      </Button>
                      <Button
                        variant="ghost"
                        size="icon"
                        className="h-7 w-7"
                        onClick={() => setBookmarks(prev => prev.filter(x => !(x.offset === b.offset && x.label === b.label)))}
                      >
                        <X className="h-3 w-3" />
                      </Button>
                    </div>
                  ))}
                </div>
                <div className="flex items-center gap-2">
                  <Button size="sm" variant="outline" onClick={() => handleCopy('zoom')}>
                    <Camera className="mr-2 h-4 w-4" />
                    Copiar zoom (texto)
                  </Button>
                  {secondarySlice && (
                    <Badge variant="outline" className="text-[11px]">
                      Comparando com: {secondarySlice.productName}
                    </Badge>
                  )}
                  {secondarySlice && (
                    <Badge variant="outline" className="text-[11px]">
                      Diferencas: {selectedSlice.sections.reduce((count, section) => {
                        const bytes = hexData.slice(section.start, section.end);
                        const compareBytes = hexData.slice(
                          secondarySlice.start + (section.start - selectedSlice.start),
                          secondarySlice.start + (section.end - selectedSlice.start),
                        );
                        const diff = bytes.filter((b, i) => compareBytes[i] && compareBytes[i].hex !== b.hex).length;
                        return count + diff;
                      }, 0)}
                    </Badge>
                  )}
                </div>
              </div>

              <div className="space-y-3">
                {selectedSlice.sections.map(section => {
                  const bytes = hexData.slice(section.start, section.end);
                  const baseOffset = section.start;
                  const compareBytes = secondarySlice
                    ? hexData.slice(
                        secondarySlice.start + (section.start - selectedSlice.start),
                        secondarySlice.start + (section.end - selectedSlice.start),
                      )
                    : null;
                  return (
                    <div
                      key={`${selectedSlice.productId}-${section.field}`}
                      className="rounded-lg border bg-gradient-to-r from-emerald-50 via-white to-amber-50/70 p-3 shadow-inner transition-all duration-200 hover:shadow-md"
                    >
                      <div className="flex items-center justify-between text-xs uppercase tracking-wider text-slate-600">
                        <span>{fieldLabels[section.field]}</span>
                        <Badge variant="outline">{bytes.length} bytes</Badge>
                      </div>
                      <div className="mt-2 flex flex-wrap gap-1 font-mono text-[11px]">
                        {bytes.map((byte, idx) => (
                          <span
                            key={idx}
                            onMouseEnter={() => setZoomHoverIndex(baseOffset + idx)}
                            onMouseLeave={() => setZoomHoverIndex(null)}
                            className={clsx(
                              'rounded px-1 transition-colors duration-150',
                              hexClass(byte.highlight, colorBlindMode),
                              zoomHoverIndex === baseOffset + idx && 'ring-2 ring-indigo-400 ring-offset-1 bg-indigo-50',
                            )}
                            title={byte.ascii === '.' ? 'Nao imprimivel' : `ASCII: ${byte.ascii}`}
                          >
                            {byte.hex}
                          </span>
                        ))}
                      </div>
                      <div className="mt-2 flex flex-wrap gap-1 rounded-md bg-slate-900/5 p-2 font-mono text-[11px] text-slate-700">
                        {bytes.map((byte, idx) => {
                          const mismatch = compareBytes && compareBytes[idx] && compareBytes[idx].hex !== byte.hex;
                          return (
                            <span
                              key={`ascii-${idx}`}
                              onMouseEnter={() => setZoomHoverIndex(baseOffset + idx)}
                              onMouseLeave={() => setZoomHoverIndex(null)}
                              title={byte.ascii === '.' ? 'Nao imprimivel' : `ASCII: ${byte.ascii}`}
                              className={clsx(
                                'rounded px-1 transition-colors',
                                mismatch && palette(colorBlindMode).update,
                                asciiClass(byte.highlight, colorBlindMode),
                                zoomHoverIndex === baseOffset + idx && 'ring-2 ring-indigo-400 ring-offset-1 bg-indigo-50',
                              )}
                            >
                              {byte.ascii}
                            </span>
                          );
                        })}
                      </div>
                      {compareBytes && (
                        <div className="mt-2 rounded-lg border bg-white/70 p-2">
                          <div className="mb-1 flex items-center justify-between text-[10px] uppercase text-slate-600">
                            <span>Comparar</span>
                            <Badge variant="outline">{compareBytes.length} bytes</Badge>
                          </div>
                          <div className="flex flex-wrap gap-1 font-mono text-[11px]">
                            {compareBytes.map((byte, idx) => {
                              const mismatch = bytes[idx] && bytes[idx].hex !== byte.hex;
                              return (
                                <span
                                  key={`cmp-${idx}`}
                                  className={clsx(
                                    'rounded px-1',
                                    mismatch && palette(colorBlindMode).update,
                                    hexClass(byte.highlight, colorBlindMode),
                                  )}
                                >
                                  {byte.hex}
                                </span>
                              );
                            })}
                          </div>
                        </div>
                      )}
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </ScrollArea>
     </CardContent>
    </Card>
  );
};

// -------------------- helpers --------------------

function convertProductsToHex(
  products: Product[],
  lastOp?: { type: 'insert' | 'update' | 'delete'; productId: string },
): { bytes: HexByte[]; slices: ProductSlice[] } {
  const bytes: HexByte[] = [];
  const slices: ProductSlice[] = [];

  const fields: { key: FieldKey; label: string; type: 'string' | 'float' | 'bool'; get: (p: Product) => any }[] = [
    { key: 'id', label: fieldLabels.id, type: 'string', get: p => p.id },
    { key: 'name', label: fieldLabels.name, type: 'string', get: p => p.name },
    { key: 'description', label: fieldLabels.description, type: 'string', get: p => p.description },
    { key: 'price', label: fieldLabels.price, type: 'float', get: p => p.price },
    { key: 'gtin', label: fieldLabels.gtin, type: 'string', get: p => p.gtin },
    { key: 'isActive', label: fieldLabels.isActive, type: 'bool', get: p => p.isActive },
    { key: 'createdAt', label: fieldLabels.createdAt, type: 'string', get: p => p.createdAt },
    { key: 'updatedAt', label: fieldLabels.updatedAt, type: 'string', get: p => p.updatedAt },
  ];

  for (const product of products) {
    const sliceStart = bytes.length;
    const sections: ProductSection[] = [];
    const highlight: HexByte['highlight'] = !product.isActive
      ? 'delete'
      : lastOp && lastOp.productId === product.id
        ? lastOp.type
        : undefined;

    for (const field of fields) {
      const fieldStart = bytes.length;
      const value = field.get(product);

      const fieldBytes =
        field.type === 'string'
          ? encodeStringLocal(value as string)
          : field.type === 'float'
            ? encodeFloat64Local(value as number)
            : encodeBooleanLocal(value as boolean);

      for (let i = 0; i < fieldBytes.length; i++) {
        const b = fieldBytes[i];
        bytes.push({
          hex: b.toString(16).padStart(2, '0').toUpperCase(),
          ascii: b >= 32 && b <= 126 ? String.fromCharCode(b) : '.',
          highlight,
          productId: product.id,
          productName: product.name,
          field: field.key,
        });
      }

      const fieldEnd = bytes.length;
      sections.push({ field: field.key, label: field.label, start: fieldStart, end: fieldEnd });
    }

    const sliceEnd = bytes.length;
    slices.push({
      productId: product.id,
      productName: product.name,
      start: sliceStart,
      end: sliceEnd,
      sections,
      highlight,
      isActive: product.isActive,
    });
  }

  // keep compatibility: encodeProducts currently concatenates; ensure we match output length
  const expectedLength = encodeProducts(products).length;
  if (expectedLength !== bytes.length) {
    console.warn('Mismatch between custom hex builder and encodeProducts length', { expectedLength, actual: bytes.length });
  }

  return { bytes, slices };
}





