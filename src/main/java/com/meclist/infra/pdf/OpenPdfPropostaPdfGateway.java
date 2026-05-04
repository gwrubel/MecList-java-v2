package com.meclist.infra.pdf;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
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

    @Override
    public byte[] gerar(PropostaPdfSnapshot snapshot) {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, output);
            document.open();

            Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            Font normal = FontFactory.getFont(FontFactory.HELVETICA, 10);

            document.add(new Paragraph("Proposta de Serviço", titulo));
            document.add(new Paragraph("Checklist #" + snapshot.checklistId(), normal));
            document.add(new Paragraph("Data de emissão: " + formatDate(snapshot.dataEmissao()), normal));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Cliente", subtitulo));
            document.add(new Paragraph("Nome: " + snapshot.clienteNome(), normal));
            document.add(new Paragraph("Documento: " + defaultText(snapshot.clienteDocumento()), normal));
            document.add(new Paragraph("Telefone: " + defaultText(snapshot.clienteTelefone()), normal));
            document.add(new Paragraph("Endereço: " + defaultText(snapshot.clienteEndereco()), normal));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Veículo", subtitulo));
            document.add(new Paragraph("Placa: " + defaultText(snapshot.placa()), normal));
            document.add(new Paragraph("Modelo: " + defaultText(snapshot.modeloVeiculo()), normal));
            document.add(new Paragraph("Marca: " + defaultText(snapshot.marcaVeiculo()), normal));
            document.add(new Paragraph("Ano: " + (snapshot.anoVeiculo() != null ? snapshot.anoVeiculo() : "-"), normal));
            document.add(new Paragraph("Quilometragem: " + (snapshot.quilometragem() != null ? snapshot.quilometragem() : 0), normal));
            document.add(new Paragraph(" "));

            document.add(new Paragraph("Itens orçados", subtitulo));
            for (PropostaPdfItemSnapshot item : snapshot.itens()) {
                document.add(new Paragraph(item.categoria() + " - " + item.nomeItem(), normal));
                document.add(new Paragraph("Mão de obra: " + formatCurrency(item.maoDeObra()), normal));

                PdfPTable tabelaProdutos = new PdfPTable(new float[] { 4, 1, 2, 2, 2 });
                tabelaProdutos.setWidthPercentage(100);
                tabelaProdutos.setSpacingBefore(6f);
                tabelaProdutos.addCell(headerCell("Produto"));
                tabelaProdutos.addCell(headerCell("Qtd"));
                tabelaProdutos.addCell(headerCell("Valor unit."));
                tabelaProdutos.addCell(headerCell("Marca"));
                tabelaProdutos.addCell(headerCell("Total"));

                for (PropostaPdfProdutoSnapshot produto : item.produtos()) {
                    tabelaProdutos.addCell(bodyCell(produto.nomeProduto()));
                    tabelaProdutos.addCell(bodyCell(String.valueOf(produto.quantidade())));
                    tabelaProdutos.addCell(bodyCell(formatCurrency(produto.valorUnitario())));
                    tabelaProdutos.addCell(bodyCell(defaultText(produto.marca())));
                    tabelaProdutos.addCell(bodyCell(formatCurrency(produto.valorTotal())));
                }

                if (item.produtos().isEmpty()) {
                    PdfPCell vazio = bodyCell("Sem produtos orçados");
                    vazio.setColspan(5);
                    tabelaProdutos.addCell(vazio);
                }

                document.add(tabelaProdutos);
                document.add(new Paragraph("Total de produtos do item: " + formatCurrency(item.totalProdutos()), normal));
                document.add(new Paragraph(" "));
            }

            document.add(new Paragraph("Resumo geral", subtitulo));
            document.add(new Paragraph("Total de mão de obra: " + formatCurrency(snapshot.totalMaoDeObra()), normal));
            document.add(new Paragraph("Total de produtos: " + formatCurrency(snapshot.totalProdutos()), normal));
            document.add(new Paragraph("Total geral: " + formatCurrency(snapshot.totalGeral()), subtitulo));

            document.close();
            return output.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Não foi possível gerar o PDF da proposta.", e);
        } catch (Exception e) {
            throw new IllegalStateException("Erro inesperado ao gerar o PDF da proposta.", e);
        }
    }

    private PdfPCell headerCell(String valor) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        PdfPCell cell = new PdfPCell(new Phrase(valor, font));
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private PdfPCell bodyCell(String valor) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 9);
        PdfPCell cell = new PdfPCell(new Phrase(defaultText(valor), font));
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private String formatCurrency(BigDecimal value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(LOCALE_PT_BR);
        return numberFormat.format(value != null ? value : BigDecimal.ZERO);
    }

    private String formatDate(java.time.LocalDate date) {
        return date != null ? DATE_FORMATTER.format(date) : "-";
    }

    private String defaultText(String value) {
        return value != null && !value.isBlank() ? value : "-";
    }
}