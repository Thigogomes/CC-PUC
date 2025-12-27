import { useState } from "react";

import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { ArrowUpRight, Github, Linkedin, Sparkles } from "lucide-react";
import { cn } from "@/lib/utils";

type SiteFooterProps = {
  onNewProduct: () => void;
  totalProducts: number;
};

const SiteFooter = ({ onNewProduct, totalProducts: _totalProducts }: SiteFooterProps) => {
  const assetBase = (import.meta.env.BASE_URL || "").replace(/\/$/, "");
  const withBase = (path: string) => `${assetBase}${path.startsWith("/") ? path : `/${path}`}`;

  const [focusedMember, setFocusedMember] = useState<string | null>(null);
  const team = [
    {
      name: "Bernardo Ladeira Borges Kartabil",
      email: "beladeira1.000@gmail.com",
      github: "https://github.com/Kartabil2908",
      linkedin: "https://www.linkedin.com/in/bernardo-ladeira-borges-kartabil",
      photo: withBase("/team/bernardo.jpg"),
    },
    {
      name: "Yasmin Moreira",
      email: "yasmiintmoreira@gmail.com",
      github: "https://github.com/yasmin-moreira",
      linkedin: "https://www.linkedin.com/in/yasmin-torres-30777933a",
      photo: withBase("/team/yasmin.jpg"),
    },
    {
      name: "Marcella Santos Belchior",
      email: "marcellabelchior28@gmail.com",
      github: "https://github.com/marcellasb28",
      linkedin: "https://www.linkedin.com/in/marcella-santos-belchior",
      photo: withBase("/team/marcela.jpg"),
    },
    {
      name: "Thiago Gomes",
      email: "thigomeslui@gmail.com",
      github: "https://github.com/Thigogomes",
      linkedin: "https://www.linkedin.com/in/thiago-gomes-211a5339a",
      photo: withBase("/team/thiago.jpg"),
    },
  ];
  const focused = focusedMember ? team.find(member => member.name === focusedMember) : null;
  const focusTarget = focused ?? team[0];

  return (
    <footer
      id="contato"
      className="relative mt-8 overflow-hidden border-t border-border bg-[radial-gradient(circle_at_20%_20%,hsl(232_26%_14%),hsl(240_30%_10%))] text-[hsl(0_0%_96%)]"
    >
      <div className="pointer-events-none absolute inset-0">
        <div className="absolute left-10 top-0 h-48 w-48 rounded-full bg-[hsl(30_92%_62%_/_35%)] blur-3xl" />
        <div className="absolute right-6 bottom-[-3rem] h-56 w-56 rounded-full bg-[hsl(200_78%_62%_/_28%)] blur-[120px]" />
        <div className="absolute inset-x-0 top-0 h-px bg-gradient-to-r from-transparent via-[hsl(30_92%_62%)] to-transparent" />
      </div>
      <div className="relative container px-4 py-6">
        <div className="flex flex-col gap-4 lg:flex-row lg:items-center">
          <div className="space-y-3">
            <p className="text-xs uppercase tracking-[0.32em] text-[hsl(220_10%_78%)]">Encerramento</p>
            <h2 className="text-2xl font-semibold text-[hsl(0_0%_100%)]">
              Fluxo encerrado com sucesso!
            </h2>
            <p className="text-[hsl(220_10%_78%)]">
              Aqui, menos é mais. Cadastre ou revise — você decide o próximo movimento.
            </p>
            <div className="flex flex-wrap items-center gap-3">
              <Button
                onClick={onNewProduct}
                className="bg-[hsl(30_92%_62%)] text-[hsl(0_0%_12%)] shadow-[0_12px_35px_hsl(30_92%_62%_/_30%)] hover:bg-[hsl(30_92%_58%)]"
              >
                Novo cadastro
                <ArrowUpRight className="h-4 w-4" />
              </Button>
              <Button
                variant="ghost"
                asChild
                className="border border-[hsl(0_0%_100%_/_18%)] text-[hsl(0_0%_96%)] hover:bg-[hsl(0_0%_100%_/_08%)] hover:text-[hsl(0_0%_96%)]"
              >
                <a href="#produtos">Voltar para a lista</a>
              </Button>
            </div>
          </div>

        </div>

        <div className="mt-6 border-t border-[hsl(0_0%_100%_/_10%)] pt-5">
          <div className="mb-4 flex items-center gap-2 text-sm uppercase tracking-[0.2em] text-[hsl(220_10%_78%)]">
            <Sparkles className="h-4 w-4 text-[hsl(30_92%_62%)]" />
            Equipe
          </div>

          <div className="relative min-h-[240px]">
            <div
              className={cn(
                "relative grid gap-4 sm:grid-cols-2 lg:grid-cols-4 transition-all duration-500 ease-out",
                focused ? "absolute inset-0 opacity-0 scale-95 pointer-events-none" : "relative opacity-100 scale-100"
              )}
            >
              {team.map(member => (
                <div
                  key={member.name}
                  role="button"
                  tabIndex={0}
                  onClick={() => setFocusedMember(member.name)}
                  onKeyDown={e => {
                    if (e.key === "Enter" || e.key === " ") {
                      e.preventDefault();
                      setFocusedMember(member.name);
                    }
                  }}
                  className="group relative cursor-pointer rounded-2xl border border-[hsl(0_0%_100%_/_12%)] bg-[hsl(0_0%_100%_/_06%)] p-3 shadow-[0_16px_40px_hsl(0_0%_0%_/_18%)] transition-all duration-300 hover:-translate-y-2 hover:scale-[1.02] hover:border-[hsl(30_92%_62%_/_45%)] hover:shadow-[0_28px_70px_hsl(30_92%_62%_/_20%)] focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[hsl(30_92%_62%_/_65%)] focus-visible:ring-offset-2 focus-visible:ring-offset-[hsl(240_30%_10%)] animate-fade-scale"
                >
                  <div className="flex items-center gap-4">
                    <Avatar className="h-12 w-12 border border-[hsl(0_0%_100%_/_12%)] bg-[hsl(30_92%_62%_/_20%)] shadow-[0_10px_30px_hsl(0_0%_0%_/_28%)] ring-2 ring-[hsl(30_92%_62%_/_50%)]">
                      <AvatarImage src={member.photo} alt={`Foto de ${member.name}`} className="object-cover" />
                      <AvatarFallback className="bg-[hsl(30_92%_62%_/_25%)] text-sm font-semibold text-[hsl(0_0%_96%)]">
                        {member.name.charAt(0)}
                      </AvatarFallback>
                    </Avatar>
                    <div>
                      <p className="text-sm font-semibold text-[hsl(0_0%_96%)]">{member.name}</p>
                      <a
                        className="text-xs text-[hsl(220_10%_78%)] transition-colors hover:text-[hsl(30_92%_62%)]"
                        href={`mailto:${member.email}`}
                      >
                        {member.email}
                      </a>
                    </div>
                  </div>
                  <div className="mt-3 flex gap-3 text-sm text-[hsl(220_10%_78%)] transition-all duration-300 group-hover:gap-4 group-hover:text-[hsl(0_0%_96%)]">
                    <a
                      className="inline-flex items-center gap-1 transition-colors hover:text-[hsl(30_92%_62%)]"
                      href={member.github}
                      target="_blank"
                      rel="noreferrer"
                    >
                      <Github className="h-4 w-4" />
                      GitHub
                    </a>
                    <a
                      className="inline-flex items-center gap-1 transition-colors hover:text-[hsl(30_92%_62%)]"
                      href={member.linkedin}
                      target="_blank"
                      rel="noreferrer"
                    >
                      <Linkedin className="h-4 w-4" />
                      LinkedIn
                    </a>
                  </div>
                  <div className="mt-3 text-xs text-[hsl(220_10%_78%)] opacity-0 transition-all duration-300 group-hover:translate-y-0 group-hover:opacity-100">
                    Clique para expandir este integrante e ocultar os demais.
                  </div>
                </div>
              ))}
            </div>

            <div
              className={cn(
                "absolute inset-0 rounded-3xl border border-[hsl(30_92%_62%_/_55%)] bg-[hsl(0_0%_100%_/_10%)] p-6 md:p-7 shadow-[0_60px_160px_hsl(30_92%_62%_/_32%)] backdrop-blur transition-all duration-500 ease-out",
                focused ? "opacity-100 scale-100" : "opacity-0 scale-95 pointer-events-none"
              )}
            >
              <div className="grid gap-6 md:grid-cols-[auto,1fr] md:items-center md:gap-8 animate-fade-scale">
                <div className="flex items-center gap-6">
                  <Avatar className="h-40 w-40 border border-[hsl(0_0%_100%_/_18%)] bg-[hsl(30_92%_62%_/_20%)] shadow-[0_40px_110px_hsl(0_0%_0%_/_45%)] ring-4 ring-[hsl(30_92%_62%_/_60%)]">
                    <AvatarImage src={focusTarget.photo} alt={`Foto de ${focusTarget.name}`} className="object-cover" />
                    <AvatarFallback className="bg-[hsl(30_92%_62%_/_25%)] text-lg font-semibold text-[hsl(0_0%_96%)]">
                      {focusTarget.name.charAt(0)}
                    </AvatarFallback>
                  </Avatar>
                  <div>
                    <p className="text-lg font-semibold text-[hsl(0_0%_96%)]">{focusTarget.name}</p>
                    <a
                      className="text-sm text-[hsl(220_10%_78%)] transition-colors hover:text-[hsl(30_92%_62%)]"
                      href={`mailto:${focusTarget.email}`}
                    >
                      {focusTarget.email}
                    </a>
                    <div className="mt-3 flex flex-wrap gap-3 text-sm text-[hsl(220_10%_78%)]">
                      <a
                        className="inline-flex items-center gap-1 transition-colors hover:text-[hsl(30_92%_62%)]"
                        href={focusTarget.github}
                        target="_blank"
                        rel="noreferrer"
                      >
                        <Github className="h-4 w-4" />
                        GitHub
                      </a>
                      <a
                        className="inline-flex items-center gap-1 transition-colors hover:text-[hsl(30_92%_62%)]"
                        href={focusTarget.linkedin}
                        target="_blank"
                        rel="noreferrer"
                      >
                        <Linkedin className="h-4 w-4" />
                        LinkedIn
                      </a>
                    </div>
                  </div>
                </div>
                <div className="flex flex-col gap-2 text-[hsl(220_10%_78%)] md:justify-self-start">
                  <p className="max-w-xl text-sm leading-relaxed">
                    Seção em foco: mostramos só este integrante para destacar contatos e foto em alta. Clique no botão para voltar à lista.
                  </p>
                  <Button
                    variant="secondary"
                    className="self-start bg-[hsl(30_92%_62%)] text-[hsl(0_0%_10%)] hover:bg-[hsl(30_92%_56%)]"
                    onClick={() => setFocusedMember(null)}
                  >
                    Ver todos os integrantes
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className="mt-6 flex flex-wrap items-center justify-between gap-4 border-t border-[hsl(0_0%_100%_/_12%)] pt-4 text-sm text-[hsl(220_10%_78%)]">
          <div className="flex items-center gap-3">
            <Sparkles className="h-5 w-5 text-[hsl(30_92%_62%)]" />
            <span className="font-medium text-[hsl(0_0%_92%)]">Interface pensada para AEDS III</span>
          </div>
          <p className="text-[hsl(0_0%_88%)]">Pronto para gravar a próxima entrada.</p>
        </div>
      </div>
    </footer>
  );
};

export { SiteFooter };
