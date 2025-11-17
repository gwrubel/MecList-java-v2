package com.meclist.validator;

import java.util.regex.Pattern;

public class ValidatorUsuario {

    // Padrões regex para validação
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private static final Pattern TELEFONE_PATTERN = Pattern.compile(
        "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$"
    );
    

    public static void validarSenha(String senha) {
        if (!isSenhaValida(senha)) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres!");
        }
    }

    public static boolean isSenhaValida(String senha) {
        if (senha == null || senha.trim().isEmpty()) {
            return false;
        }
        return senha.length() >= 6 && senha.length() <= 128;
    }

    public static void validarEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido!");
        }
    }

    public static boolean isEmailValido(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static void validarTelefone(String telefone) {
        if (!isTelefoneValido(telefone)) {
            throw new IllegalArgumentException("Telefone inválido! Use o formato: (XX) XXXXX-XXXX ou (XX) XXXX-XXXX");
        }
    }

    public static boolean isTelefoneValido(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        return TELEFONE_PATTERN.matcher(telefone.trim()).matches();
    }

    public static void validarCpf(String cpf) {
        if (!isCpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        // Verifica se tem exatamente 11 dígitos
        if (!cpfLimpo.matches("\\d{11}")) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (CPF inválido)
        if (cpfLimpo.chars().distinct().count() == 1) {
            return false;
        }
        
        // Validação do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }
        
        if (primeiroDigito != Character.getNumericValue(cpfLimpo.charAt(9))) {
            return false;
        }
        
        // Validação do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }
        
        return segundoDigito == Character.getNumericValue(cpfLimpo.charAt(10));
    }

    public static void validarCnpj(String cnpj) {
        if (!isCnpjValido(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido!");
        }
    }

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        
        // Verifica se tem exatamente 14 dígitos
        if (!cnpjLimpo.matches("\\d{14}")) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (CNPJ inválido)
        if (cnpjLimpo.chars().distinct().count() == 1) {
            return false;
        }
        
        // Validação do primeiro dígito verificador
        int soma = 0;
        int peso = 2;
        for (int i = 11; i >= 0; i--) {
            soma += Character.getNumericValue(cnpjLimpo.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito >= 10) {
            primeiroDigito = 0;
        }
        
        if (primeiroDigito != Character.getNumericValue(cnpjLimpo.charAt(12))) {
            return false;
        }
        
        // Validação do segundo dígito verificador
        soma = 0;
        peso = 2;
        for (int i = 12; i >= 0; i--) {
            soma += Character.getNumericValue(cnpjLimpo.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito >= 10) {
            segundoDigito = 0;
        }
        
        return segundoDigito == Character.getNumericValue(cnpjLimpo.charAt(13));
    }

    public static void validarCpfOuCnpj(String documento) {
        if (documento == null) {
            throw new IllegalArgumentException("Documento não pode ser nulo!");
        }
        
        String documentoLimpo = documento.replaceAll("\\D", "");
        
        if (documentoLimpo.length() == 11) {
            validarCpf(documentoLimpo);
        } else if (documentoLimpo.length() == 14) {
            validarCnpj(documentoLimpo);
        } else {
            throw new IllegalArgumentException("Documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ)!");
        }
    }

    /**
     * Valida um documento baseado no tipo especificado
     * @param documento o documento a ser validado
     * @param tipoDocumento o tipo do documento (CPF ou CNPJ)
     */
    public static void validarDocumentoPorTipo(String documento, com.meclist.domain.enums.TipoDocumento tipoDocumento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new IllegalArgumentException("Documento não pode ser nulo ou vazio!");
        }
        
        String documentoLimpo = documento.replaceAll("\\D", "");
        
        switch (tipoDocumento) {
            case CPF:
                if (documentoLimpo.length() != 11) {
                    throw new IllegalArgumentException("CPF deve ter exatamente 11 dígitos!");
                }
                validarCpf(documentoLimpo);
                break;
            case CNPJ:
                if (documentoLimpo.length() != 14) {
                    throw new IllegalArgumentException("CNPJ deve ter exatamente 14 dígitos!");
                }
                validarCnpj(documentoLimpo);
                break;
            default:
                throw new IllegalArgumentException("Tipo de documento inválido: " + tipoDocumento);
        }
    }

    /**
     * Verifica se um documento é válido baseado no tipo especificado
     * @param documento o documento a ser validado
     * @param tipoDocumento o tipo do documento (CPF ou CNPJ)
     * @return true se o documento for válido, false caso contrário
     */
    public static boolean isDocumentoValidoPorTipo(String documento, com.meclist.domain.enums.TipoDocumento tipoDocumento) {
        try {
            validarDocumentoPorTipo(documento, tipoDocumento);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Formata um documento para exibição
     * @param documento o documento a ser formatado
     * @param tipoDocumento o tipo do documento
     * @return o documento formatado
     */
    public static String formatarDocumento(String documento, com.meclist.domain.enums.TipoDocumento tipoDocumento) {
        if (documento == null || documento.trim().isEmpty()) {
            return documento;
        }
        
        String documentoLimpo = documento.replaceAll("\\D", "");
        
        switch (tipoDocumento) {
            case CPF:
                if (documentoLimpo.length() == 11) {
                    return documentoLimpo.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
                }
                break;
            case CNPJ:
                if (documentoLimpo.length() == 14) {
                    return documentoLimpo.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
                }
                break;
        }
        
        return documentoLimpo;
    }

    /**
     * Valida dados completos de um cliente
     * @param nome nome do cliente
     * @param email email do cliente
     * @param senha senha do cliente
     * @param telefone telefone do cliente
     * @param documento documento do cliente
     * @param tipoDocumento tipo do documento
     * @param endereco endereço do cliente
     */
    public static void validarDadosCompletosCliente(String nome, String email, String senha, 
                                                   String telefone, String documento, 
                                                   com.meclist.domain.enums.TipoDocumento tipoDocumento, 
                                                   String endereco) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório!");
        }
        
        validarEmail(email);
        validarSenha(senha);
        validarTelefone(telefone);
        validarDocumentoPorTipo(documento, tipoDocumento);
        
        if (endereco == null || endereco.trim().isEmpty()) {
            throw new IllegalArgumentException("Endereço é obrigatório!");
        }
    }

    /**
     * Valida dados completos de um mecânico (apenas CPF)
     * @param nome nome do mecânico
     * @param email email do mecânico
     * @param senha senha do mecânico
     * @param telefone telefone do mecânico
     * @param cpf CPF do mecânico
     */
    public static void validarDadosCompletosMecanico(String nome, String email, String senha, 
                                                    String telefone, String cpf) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório!");
        }
        
        validarEmail(email);
        validarSenha(senha);
        validarTelefone(telefone);
        validarCpf(cpf);
    }

    /**
     * Valida dados de atualização de um mecânico
     * @param nome nome do mecânico (opcional)
     * @param email email do mecânico (opcional)
     * @param senha senha do mecânico (opcional)
     * @param telefone telefone do mecânico (opcional)
     * @param cpf CPF do mecânico (opcional)
     */
    public static void validarDadosAtualizacaoMecanico(String nome, String email, String senha, 
                                                      String telefone, String cpf) {
        // Validar nome se fornecido
        if (nome != null && nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio!");
        }
        
        // Validar email se fornecido
        if (email != null) {
            validarEmail(email);
        }
        
        // Validar senha se fornecida
        if (senha != null) {
            validarSenha(senha);
        }
        
        // Validar telefone se fornecido
        if (telefone != null) {
            validarTelefone(telefone);
        }
        
        // Validar CPF se fornecido
        if (cpf != null) {
            validarCpf(cpf);
        }
    }

    /**
     * Formata CPF para exibição
     * @param cpf o CPF a ser formatado
     * @return o CPF formatado
     */
    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return cpf;
        }
        
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        if (cpfLimpo.length() == 11) {
            return cpfLimpo.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        }
        
        return cpfLimpo;
    }

    /**
     * Limpa um CPF removendo caracteres não numéricos
     * @param cpf o CPF a ser limpo
     * @return o CPF apenas com números
     */
    public static String limparCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return cpf;
        }
        
        return cpf.replaceAll("\\D", "");
    }
}