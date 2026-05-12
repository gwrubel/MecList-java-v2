package com.meclist.infra.pdf;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.meclist.dto.checklist.administrativo.PropostaPdfItemSnapshot;
import com.meclist.dto.checklist.administrativo.PropostaPdfProdutoSnapshot;
import com.meclist.dto.checklist.administrativo.PropostaPdfSnapshot;
import com.meclist.interfaces.PropostaPdfGateway;

@Component
public class OpenPdfPropostaPdfGateway implements PropostaPdfGateway {

    private static final Locale LOCALE_PT_BR = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Cores para o layout
    private static final Color COR_PRIMARIA = new Color(40, 44, 52); // Cinza Escuro Profissional
    private static final Color COR_SUBTITULO = new Color(70, 70, 70);
    private static final Color COR_FUNDO_CABECALHO = new Color(240, 240, 240);
    private static final Color COR_LARANJA_MEC = new Color(216, 107, 6);

    @Override
    public byte[] gerar(PropostaPdfSnapshot snapshot) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, output);
            
            // Adiciona o título como metadado do arquivo PDF
            document.addTitle("Orcamento_Meclist_" + snapshot.checklistId());
            document.open();

            // Fontes
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COR_PRIMARIA);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, COR_SUBTITULO);
            Font fontNormalBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
            Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 9);

            // --- 1. CABEÇALHO (LOGO + DADOS EMPRESA) ---
            PdfPTable headerTable = new PdfPTable(2);
            headerTable.setWidthPercentage(100);
            headerTable.setWidths(new float[]{1, 1});

            // Lado Esquerdo: Logo PNG ou Fallback (MEC laranja + LIST preto)
            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            
            try {
                Image logoIcon = carregarLogoMarca();
                if (logoIcon != null) {
                    logoIcon.scaleToFit(120, 60); // Ajuste o tamanho conforme sua logo PNG
                    logoCell.addElement(logoIcon);
                } else {
                    logoCell.addElement(gerarLogoFallback());
                }
            } catch (Exception e) {
                // Em caso de erro na leitura, aplica o fallback
                logoCell.addElement(gerarLogoFallback());
            }
            headerTable.addCell(logoCell);

            // Lado Direito: Dados da Oficina e Proposta
            PdfPCell infoEmpresa = new PdfPCell();
            infoEmpresa.setBorder(Rectangle.NO_BORDER);
            infoEmpresa.setHorizontalAlignment(Element.ALIGN_RIGHT);
            infoEmpresa.addElement(new Paragraph("Orçamento de Serviço: #" + snapshot.checklistId(), fontNormalBold));
            infoEmpresa.addElement(new Paragraph("Emissão: " + formatDate(snapshot.dataEmissao()), fontNormal));
            headerTable.addCell(infoEmpresa);
            document.add(headerTable);

            document.add(new Paragraph(" ")); // Espaçador
            document.add(new Paragraph(new Phrase("PROPOSTA DE SERVIÇO AUTOMOTIVO", fontSubtitulo)));
            document.add(new Paragraph("----------------------------------------------------------------------------------------------------------------------------------", fontNormal));

            // --- 2. DADOS DO CLIENTE E VEÍCULO (Lado a Lado) ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10f);

            // Coluna Cliente
            PdfPCell clienteCell = new PdfPCell();
            clienteCell.setBorder(Rectangle.NO_BORDER);
            clienteCell.addElement(new Phrase("CLIENTE", fontSubtitulo));
            clienteCell.addElement(new Paragraph("Nome: " + snapshot.clienteNome(), fontNormal));
            clienteCell.addElement(new Paragraph("Doc: " + defaultText(snapshot.clienteDocumento()), fontNormal));
            clienteCell.addElement(new Paragraph("Tel: " + defaultText(snapshot.clienteTelefone()), fontNormal));
            infoTable.addCell(clienteCell);

            // Coluna Veículo
            PdfPCell veiculoCell = new PdfPCell();
            veiculoCell.setBorder(Rectangle.NO_BORDER);
            veiculoCell.addElement(new Phrase("VEÍCULO", fontSubtitulo));
            veiculoCell.addElement(new Paragraph("Placa: " + defaultText(snapshot.placa()) + " | Modelo: " + defaultText(snapshot.modeloVeiculo()), fontNormal));
            veiculoCell.addElement(new Paragraph("Ano: " + (snapshot.anoVeiculo() != null ? snapshot.anoVeiculo() : "-"), fontNormal));
            veiculoCell.addElement(new Paragraph("KM: " + (snapshot.quilometragem() != null ? snapshot.quilometragem() : "0"), fontNormal));
            infoTable.addCell(veiculoCell);
            document.add(infoTable);

            document.add(new Paragraph(" "));

            // --- 3. ITENS E PRODUTOS ---
            document.add(new Paragraph("DETALHAMENTO DOS SERVIÇOS E PEÇAS", fontSubtitulo));
            
            for (PropostaPdfItemSnapshot item : snapshot.itens()) {
                document.add(new Paragraph(" "));
                
                // Cabeçalho do Item (Categoria - Serviço)
                PdfPTable itemHeader = new PdfPTable(1);
                itemHeader.setWidthPercentage(100);
                PdfPCell cellH = new PdfPCell(new Phrase(item.categoria().replace("_", " ") + " - " + item.nomeItem(), fontNormalBold));
                cellH.setBackgroundColor(COR_FUNDO_CABECALHO);
                cellH.setPadding(5f);
                cellH.setBorder(Rectangle.BOTTOM);
                itemHeader.addCell(cellH);
                document.add(itemHeader);

                // Tabela de Peças do Item
                PdfPTable tabelaProdutos = new PdfPTable(new float[] { 4, 1, 2, 2, 2 });
                tabelaProdutos.setWidthPercentage(100);
                tabelaProdutos.addCell(styledHeaderCell("Produto/Peça"));
                tabelaProdutos.addCell(styledHeaderCell("Qtd"));
                tabelaProdutos.addCell(styledHeaderCell("V. Unit"));
                tabelaProdutos.addCell(styledHeaderCell("Marca"));
                tabelaProdutos.addCell(styledHeaderCell("Total"));

                if (item.produtos().isEmpty()) {
                    PdfPCell vazio = new PdfPCell(new Phrase("Nenhuma peça necessária para este item.", fontNormal));
                    vazio.setColspan(5);
                    vazio.setPadding(5f);
                    vazio.setBorder(Rectangle.BOX);
                    tabelaProdutos.addCell(vazio);
                } else {
                    for (PropostaPdfProdutoSnapshot produto : item.produtos()) {
                        tabelaProdutos.addCell(styledBodyCell(produto.nomeProduto(), Element.ALIGN_LEFT));
                        tabelaProdutos.addCell(styledBodyCell(String.valueOf(produto.quantidade()), Element.ALIGN_CENTER));
                        tabelaProdutos.addCell(styledBodyCell(formatCurrency(produto.valorUnitario()), Element.ALIGN_RIGHT));
                        tabelaProdutos.addCell(styledBodyCell(produto.marca(), Element.ALIGN_CENTER));
                        tabelaProdutos.addCell(styledBodyCell(formatCurrency(produto.valorTotal()), Element.ALIGN_RIGHT));
                    }
                }
                document.add(tabelaProdutos);

                // Rodapé do Item (Mão de obra)
                Paragraph pMaoDeObra = new Paragraph("Subtotal do Item: Mão de Obra " + formatCurrency(item.maoDeObra()) + 
                                       " + Peças " + formatCurrency(item.totalProdutos()), fontNormal);
                pMaoDeObra.setAlignment(Element.ALIGN_RIGHT);
                document.add(pMaoDeObra);
            }

            // --- 4. RESUMO FINAL ---
            document.add(new Paragraph(" "));
            document.add(new Paragraph(" "));
            
            PdfPTable resumoTable = new PdfPTable(2);
            resumoTable.setWidthPercentage(40);
            resumoTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            resumoTable.addCell(simpleCell("Total Mão de Obra:", Element.ALIGN_LEFT, fontNormal));
            resumoTable.addCell(simpleCell(formatCurrency(snapshot.totalMaoDeObra()), Element.ALIGN_RIGHT, fontNormalBold));

            resumoTable.addCell(simpleCell("Total Peças/Produtos:", Element.ALIGN_LEFT, fontNormal));
            resumoTable.addCell(simpleCell(formatCurrency(snapshot.totalProdutos()), Element.ALIGN_RIGHT, fontNormalBold));

            PdfPCell cellTotalTxt = simpleCell("TOTAL GERAL:", Element.ALIGN_LEFT, fontSubtitulo);
            cellTotalTxt.setBorder(Rectangle.TOP);
            resumoTable.addCell(cellTotalTxt);

            PdfPCell cellTotalVlr = simpleCell(formatCurrency(snapshot.totalGeral()), Element.ALIGN_RIGHT, fontTitulo);
            cellTotalVlr.setBorder(Rectangle.TOP);
            resumoTable.addCell(cellTotalVlr);

            document.add(resumoTable);

            // --- 5. VALIDADE E ASSINATURA ---
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Validade da proposta: 5 dias úteis.", fontNormal));
            
            document.close();
            return output.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar PDF profissional.", e);
        }
    }

    // --- MÉTODOS AUXILIARES DE LOGO ---
    
    private Image carregarLogoMarca() {
        try (InputStream pngStream = getClass().getResourceAsStream("/static/logo.png")) {
            if (pngStream == null) return null;
            byte[] imageBytes = pngStream.readAllBytes();
            return Image.getInstance(imageBytes);
        } catch (Exception e) {
            return null; // Retorna null silenciosamente para ativar o fallback
        }
    }

    private Paragraph gerarLogoFallback() {
        Font fontMec = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COR_LARANJA_MEC);
        Font fontList = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLACK);
        
        Phrase brandPhrase = new Phrase();
        brandPhrase.add(new Chunk("MEC", fontMec));
        brandPhrase.add(new Chunk("LIST", fontList));
        
        return new Paragraph(brandPhrase);
    }

    // --- MÉTODOS AUXILIARES DE ESTILIZAÇÃO ---

    private PdfPCell styledHeaderCell(String valor) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 8, Color.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(valor, font));
        cell.setBackgroundColor(COR_PRIMARIA);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(4f);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private PdfPCell styledBodyCell(String valor, int alinhamento) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 8);
        PdfPCell cell = new PdfPCell(new Phrase(defaultText(valor), font));
        cell.setHorizontalAlignment(alinhamento);
        cell.setPadding(4f);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private PdfPCell simpleCell(String valor, int alinhamento, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(valor, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alinhamento);
        cell.setPadding(2f);
        return cell;
    }

    private String formatCurrency(BigDecimal value) {
        return NumberFormat.getCurrencyInstance(LOCALE_PT_BR).format(value != null ? value : BigDecimal.ZERO);
    }

    private String formatDate(java.time.LocalDate date) {
        return date != null ? DATE_FORMATTER.format(date) : "-";
    }

    private String defaultText(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }
}