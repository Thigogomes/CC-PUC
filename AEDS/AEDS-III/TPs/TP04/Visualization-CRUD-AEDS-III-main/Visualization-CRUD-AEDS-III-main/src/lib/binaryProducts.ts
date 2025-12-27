// src/lib/binaryProducts.ts
import type { Product } from '@/types/product';

// UTF-8 explícito
const encoder = new TextEncoder();
const decoder = new TextDecoder("utf-8");

// ---------- helpers para string ----------

function encodeString(value: string): Uint8Array {
  const strBytes = encoder.encode(value); // UTF-8
  const buf = new Uint8Array(2 + strBytes.length);
  const view = new DataView(buf.buffer);

  view.setUint16(0, strBytes.length, true);
  buf.set(strBytes, 2);

  return buf;
}

function readString(bytes: Uint8Array, offsetRef: { value: number }): string {
  const view = new DataView(bytes.buffer, bytes.byteOffset, bytes.byteLength);
  const offset = offsetRef.value;

  if (offset + 2 > bytes.length) {
    throw new Error('Buffer truncado ao ler tamanho da string');
  }

  const length = view.getUint16(offset, true);
  const start = offset + 2;
  const end = start + length;

  if (end > bytes.length) {
    throw new Error('Buffer truncado ao ler conteúdo da string');
  }

  const slice = bytes.subarray(start, end);
  offsetRef.value = end;

  return decoder.decode(slice); // UTF-8
}

// ---------- helpers numérico / boolean ----------

function encodeFloat64(value: number): Uint8Array {
  const buf = new ArrayBuffer(8);
  const view = new DataView(buf);
  view.setFloat64(0, value, true);
  return new Uint8Array(buf);
}

function readFloat64(bytes: Uint8Array, offsetRef: { value: number }): number {
  const offset = offsetRef.value;
  if (offset + 8 > bytes.length) {
    throw new Error('Buffer truncado ao ler float64');
  }
  const view = new DataView(bytes.buffer, bytes.byteOffset + offset, 8);
  const value = view.getFloat64(0, true);
  offsetRef.value = offset + 8;
  return value;
}

function encodeBoolean(value: boolean): Uint8Array {
  return new Uint8Array([value ? 1 : 0]);
}

function readBoolean(bytes: Uint8Array, offsetRef: { value: number }): boolean {
  const offset = offsetRef.value;
  if (offset + 1 > bytes.length) {
    throw new Error('Buffer truncado ao ler boolean');
  }
  const b = bytes[offset];
  offsetRef.value = offset + 1;
  return b !== 0;
}

// ---------- encode/decode de Product ----------

export function encodeProduct(p: Product): Uint8Array {
  const parts: Uint8Array[] = [
    encodeString(p.id),
    encodeString(p.name),
    encodeString(p.description),
    encodeFloat64(p.price),
    encodeString(p.gtin),
    encodeBoolean(p.isActive),
    encodeString(p.createdAt),
    encodeString(p.updatedAt),
  ];

  const totalLength = parts.reduce((sum, part) => sum + part.length, 0);
  const result = new Uint8Array(totalLength);

  let offset = 0;
  for (const part of parts) {
    result.set(part, offset);
    offset += part.length;
  }

  return result;
}

export function encodeProducts(products: Product[]): Uint8Array {
  const chunks = products.map(encodeProduct);
  const totalLength = chunks.reduce((sum, chunk) => sum + chunk.length, 0);

  const result = new Uint8Array(totalLength);
  let offset = 0;

  for (const chunk of chunks) {
    result.set(chunk, offset);
    offset += chunk.length;
  }

  return result;
}

export function decodeProducts(bytes: Uint8Array): Product[] {
  const products: Product[] = [];
  const offsetRef = { value: 0 };

  while (offsetRef.value < bytes.length) {
    const id = readString(bytes, offsetRef);
    const name = readString(bytes, offsetRef);
    const description = readString(bytes, offsetRef);
    const price = readFloat64(bytes, offsetRef);
    const gtin = readString(bytes, offsetRef);
    const isActive = readBoolean(bytes, offsetRef);
    const createdAt = readString(bytes, offsetRef);
    const updatedAt = readString(bytes, offsetRef);

    products.push({
      id,
      name,
      description,
      price,
      gtin,
      isActive,
      createdAt,
      updatedAt,
    });
  }

  return products;
}

// ---------- HEX helpers ----------

export function bytesToHex(bytes: Uint8Array): string {
  return Array.from(bytes)
    .map(b => b.toString(16).padStart(2, '0'))
    .join('');
}

export function hexToBytes(hex: string): Uint8Array {
  const clean = hex.replace(/\s+/g, '');

  if (clean.length % 2 !== 0) {
    throw new Error('Hex inválido: tamanho ímpar');
  }

  const result = new Uint8Array(clean.length / 2);

  for (let i = 0; i < result.length; i++) {
    result[i] = parseInt(clean.substring(i * 2, i * 2 + 2), 16);
  }

  return result;
}
