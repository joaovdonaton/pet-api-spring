package br.pucpr.petapi;

import br.pucpr.petapi.lib.location.dto.response.CEPDataResponse;
import br.pucpr.petapi.lib.location.dto.response.geocoding.CoordinatesDTO;
import br.pucpr.petapi.rest.adoptionProfiles.AdoptionProfile;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


/**
 * Dados dessa classe estão organizados pelo índice.
 * e.g os dados do índice 0 do cepDataResponses complementa o do índice 0 do coordinatesDTOs
 */
@Data
public class TestDataLoader {
    private final List<CEPDataResponse> cepDataResponses = List.of(
            new CEPDataResponse(
                    "80250-220",
                    "Avenida Visconde de Guarapuava",
                    "de 3337/3338 a 4379/4380",
                    "Centro",
                    "Curitiba",
                    "PR",
                    "4106902",
                    "",
                    "41",
                    "7535"
            ),
            new CEPDataResponse(
                    "82410-530",
                    "Avenida Doutor Eugênio Bertolli",
                    "",
                    "Santa Felicidade",
                    "Curitiba",
                    "PR",
                    "4106902",
                    "",
                    "41",
                    "7535"
            )
    );

    private final List<CoordinatesDTO> coordinatesDTOs = List.of(
            new CoordinatesDTO(
                    "-25.437242700000",
                    "-49.269966500000"
            ),
            new CoordinatesDTO(
                    "-25.400720900000",
                    "-49.334472800000"
            )
    );

    private final List<AdoptionProfile> adoptionProfiles = List.of(
            new AdoptionProfile(
                    null,
                    "80250-220",
                    "I like cats and dogs and cats and dogs and cats and dogs and cats and dogs",
                    false,
                    "PR",
                    "Curitiba",
                    "Centro",
                    new BigDecimal(coordinatesDTOs.get(0).getLat()),
                    new BigDecimal(coordinatesDTOs.get(0).getLng()),
                    null,
                    null
            )
    );
}
