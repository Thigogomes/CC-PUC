import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ArrowUpRight, Sparkles } from "lucide-react";

type SiteHeaderProps = {
  onNewProduct: () => void;
  totalProducts: number;
};

const SiteHeader = ({ onNewProduct, totalProducts }: SiteHeaderProps) => {
  return (
    <header className="relative overflow-hidden border-b border-border bg-gradient-to-br from-[hsl(204_83%_97%)] via-[hsl(210_68%_95%)] to-[hsl(18_92%_94%)] text-foreground">
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute -left-20 top-10 h-44 w-44 rounded-full bg-[hsl(200_90%_92%)] blur-3xl" />
        <div className="absolute right-10 top-[-4rem] h-60 w-60 rounded-full bg-[hsl(18_92%_88%)] blur-[100px]" />
        <div className="absolute bottom-[-8rem] left-1/2 h-72 w-72 -translate-x-1/2 rounded-full bg-[hsl(142_60%_86%)] blur-[110px]" />
        <div className="absolute inset-x-0 bottom-0 h-px bg-gradient-to-r from-transparent via-[hsl(200_60%_70%)] to-transparent" />
      </div>
      <div className="relative container px-4 py-12">
        <div className="grid gap-10 lg:grid-cols-[1.2fr_0.8fr] lg:items-center">
          <div className="space-y-6">
            <div className="inline-flex items-center gap-2 rounded-full border border-border/70 bg-[hsl(0_0%_100%_/_65%)] px-3 py-1 text-xs font-medium uppercase tracking-[0.28em] text-muted-foreground backdrop-blur">
              <span className="h-2 w-2 rounded-full bg-[hsl(160_76%_46%)] shadow-[0_0_0_6px_hsl(160_76%_46%_/_15%)]" />
              Visual limpo
            </div>
            <div className="flex items-center gap-3">
              <div className="grid h-12 w-12 place-items-center rounded-2xl bg-[hsl(200_75%_92%)] text-primary shadow-lg shadow-primary/15">
                <Sparkles className="h-6 w-6" />
              </div>
              <div>
                <p className="text-xs uppercase tracking-[0.32em] text-muted-foreground">Painel de produtos</p>
                <h1 className="text-3xl font-semibold leading-tight text-primary md:text-4xl">Data Flow</h1>
              </div>
            </div>
            <p className="text-lg text-muted-foreground max-w-2xl">
              Cadastre e explore produtos com foco no aprendizado, feedback rápido e visual leve. Tudo isso em um único painel!
            </p>
            <div className="flex flex-wrap items-center gap-3">
              <Button size="lg" onClick={onNewProduct} className="shadow-lg shadow-primary/20">
                Cadastrar produto
                <ArrowUpRight className="h-4 w-4" />
              </Button>
              <Button
                asChild
                size="lg"
                variant="secondary"
                className="flex items-center gap-2 bg-[linear-gradient(120deg,hsl(45_94%_65%),hsl(35_90%_62%))] text-[hsl(26_38%_16%)] hover:brightness-105 border border-[hsl(42_80%_58%)] shadow-[0_14px_32px_hsl(42_82%_48%_/_32%)]"
              >
                <a
                  href="https://docs.google.com/forms/d/e/1FAIpQLScRS8AXb3D58Z76fgs0mAoiUJ6W98quJSxRh9ysnVIax9dJ1Q/viewform?usp=header"
                  target="_blank"
                  rel="noreferrer"
                >
                  <span className="font-semibold tracking-wide">Nos avalie!</span>
                  <span aria-hidden className="text-lg">❤️</span>
                </a>
              </Button>
            </div>
          </div>

          <div className="relative">
            <div className="absolute -inset-4 -z-10 rounded-[32px] bg-[radial-gradient(circle_at_20%_20%,hsl(200_90%_92%),hsl(210_70%_94%))] opacity-80 blur-3xl" />
            <div className="rounded-[28px] border border-border/80 bg-[hsl(0_0%_100%_/_80%)] p-6 shadow-2xl shadow-primary/15 backdrop-blur">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-xs uppercase tracking-[0.3em] text-muted-foreground">Agora</p>
                  <p className="text-lg font-semibold text-primary">Visão rápida</p>
                </div>
                <Badge className="flex items-center gap-2 border-transparent bg-transparent text-foreground shadow-none hover:bg-transparent">
                  <span className="h-3.5 w-3.5 rounded-full bg-[hsl(160_76%_46%)] shadow-[0_0_0_8px_hsl(160_76%_46%_/_12%)]" />
                </Badge>
              </div>
              <div className="mt-6 space-y-4">
                <div className="rounded-2xl border border-border/70 bg-[hsl(0_0%_100%_/_75%)] px-4 py-3 shadow-sm">
                  <p className="text-xs uppercase tracking-wide text-muted-foreground">Produtos ativos</p>
                  <p className="text-3xl font-semibold text-primary">{totalProducts}</p>
                  <p className="mt-1 text-xs text-muted-foreground">Sincronizado em memória.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </header>
  );
};

export { SiteHeader };
