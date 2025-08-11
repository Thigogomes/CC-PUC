package Classes;

import java.time.LocalDateTime;

public class ProcessamentoImagem {
    private Long id;
    private String fileName;
    private String titulo;
    private LocalDateTime processedAt;

    public ProcessamentoImagem() { }

    public ProcessamentoImagem(String fileName, String titulo, LocalDateTime processedAt) {
        this.fileName = fileName;
        this.titulo = titulo;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}