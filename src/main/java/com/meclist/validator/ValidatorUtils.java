package com.meclist.validator;

import java.util.Map;

import com.meclist.exception.CampoInvalidoException;

public class ValidatorUtils {

    public static void validarSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new CampoInvalidoException(
                    Map.of("senha", "A senha deve ter no mínimo 6 caracteres.")
            );
        }
    }

    public static void validarEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new CampoInvalidoException(
                    Map.of("email", "E-mail inválido.")
            );
        }
    }

    public static void validarTelefone(String telefone) {
        if (telefone == null || !telefone.matches("\\d{10,11}")) {
            throw new CampoInvalidoException(
                    Map.of("telefone", "Telefone inválido.")
            );
        }
    }

    public static void validarCpf(String cpf) {
        if (!isCpfValido(cpf)) {
            throw new CampoInvalidoException(
                    Map.of("cpf", "CPF inválido.")
            );
        }
    }

    public static boolean isCpfValido(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) return false;
        if (cpf.chars().distinct().count() == 1) return false;

        int soma = 0;
        for (int i = 0; i < 9; i++) soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        int d1 = 11 - (soma % 11);
        if (d1 > 9) d1 = 0;
        if (d1 != Character.getNumericValue(cpf.charAt(9))) return false;

        soma = 0;
        for (int i = 0; i < 10; i++) soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        int d2 = 11 - (soma % 11);
        if (d2 > 9) d2 = 0;

        return d2 == Character.getNumericValue(cpf.charAt(10));
    }

    public static void validarCnpj(String cnpj) {
        if (!isCnpjValido(cnpj)) {
            throw new CampoInvalidoException(
                    Map.of("cnpj", "CNPJ inválido.")
            );
        }
    }

    public static boolean isCnpjValido(String cnpj) {
        if (cnpj == null || !cnpj.matches("\\d{14}")) return false;
        if (cnpj.chars().distinct().count() == 1) return false;

        int soma = 0;
        int peso = 2;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int d1 = 11 - (soma % 11);
        if (d1 > 9) d1 = 0;
        if (d1 != Character.getNumericValue(cnpj.charAt(12))) return false;

        soma = 0;
        peso = 2;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int d2 = 11 - (soma % 11);
        if (d2 > 9) d2 = 0;

        return d2 == Character.getNumericValue(cnpj.charAt(13));
    }

    public static void validarCpfOuCnpj(String documento) {
        if (documento == null) {
            throw new CampoInvalidoException(
                    Map.of("documento", "Documento não pode ser nulo.")
            );
        }

        String limpo = documento.replaceAll("\\D", "");

        if (limpo.length() == 11) {
            validarCpf(limpo);
        } else if (limpo.length() == 14) {
            validarCnpj(limpo);
        } else {
            throw new CampoInvalidoException(
                    Map.of("documento", "Documento deve ter 11 dígitos (CPF) ou 14 dígitos (CNPJ).")
            );
        }
    }

    public static void validarPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new CampoInvalidoException(
                    Map.of("placa", "A placa não pode ser vazia.")
            );
        }

        if (!placa.matches("^[A-Z]{3}\\d{4}$")) {
            throw new CampoInvalidoException(
                    Map.of("placa", "Placa inválida. Formato esperado: ABC1234.")
            );
        }
    }
}
