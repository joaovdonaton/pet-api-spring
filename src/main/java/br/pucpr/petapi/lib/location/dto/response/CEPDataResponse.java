package br.pucpr.petapi.lib.location.dto.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CEPDataResponse {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;
    private String ibge;
    private String gia;
    private String ddd;
    private String siafi;
}
