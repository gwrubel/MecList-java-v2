package com.meclist.validator;

public class ValidatorUsuario {

    public static void validarSenha(String senha) {
        if (!isSenhaValida(senha)) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres!");
        }
    }

    public static boolean isSenhaValida(String senha) {
        return senha != null && senha.length() >= 6;
    }

    public static void validarEmail(String email) {
        if (!isEmailValido(email)) {
            throw new IllegalArgumentException("E-mail inválido!");
        }
    }

    public static boolean isEmailValido(String email) {
        return email != null && email.contains("@");
    }

    public static void validarTelefone(String telefone) {
        if (!isTelefoneValido(telefone)) {
            throw new IllegalArgumentException("Telefone inválido!");
        }
    }

    public static boolean isTelefoneValido(String telefone) {
        return telefone != null && telefone.matches("\\d{10,11}");
    }

    public static void validarCpf(String cpf) {
        if (!isCpfValido(cpf)) {
            throw new IllegalArgumentException("CPF inválido!");
        }
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}"))
            return false;

        if (cpf.chars().distinct().count() == 1)
            return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9)
            primeiroDigito = 0;

        if (primeiroDigito != Character.getNumericValue(cpf.charAt(9)))
            return false;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9)
            segundoDigito = 0;

        return segundoDigito == Character.getNumericValue(cpf.charAt(10));
    }

    public static void validarCnpj(String cnpj) {
        if (!isCnpjValido(cnpj)) {
            throw new IllegalArgumentException("CNPJ inválido!");
        }
    }

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}"))
            return false;

        if (cnpj.chars().distinct().count() == 1)
            return false;

        // Validação do primeiro dígito verificador
        int soma = 0;
        int peso = 2;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int primeiroDigito = 11 - (soma % 11);
        if (primeiroDigito > 9)
            primeiroDigito = 0;

        if (primeiroDigito != Character.getNumericValue(cnpj.charAt(12)))
            return false;

        // Validação do segundo dígito verificador
        soma = 0;
        peso = 2;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int segundoDigito = 11 - (soma % 11);
        if (segundoDigito > 9)
            segundoDigito = 0;

        return segundoDigito == Character.getNumericValue(cnpj.charAt(13));
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
}