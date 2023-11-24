package mapa.SudestePR.ValidationDB;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import mapa.SudestePR.Entity.Cidade;
import mapa.SudestePR.Entity.CustomResponse;
import mapa.SudestePR.Entity.Rota;
import mapa.SudestePR.Repository.CidadeRepository;
import mapa.SudestePR.Repository.RotaRepository;

@Service
public class ValidationService {
	
	@Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private RotaRepository rotaRepository;

    public CustomResponse postAll() throws FileNotFoundException, IOException {
        List<Cidade> cidades = new ArrayList<>();
        List<Rota> rotas = new ArrayList<>();

        File dataSource = new File("src\\main\\resources\\dataSource.txt");
        if (!dataSource.exists()) {
            return new CustomResponse(false, "File not found!");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(dataSource))) {
            String line;
            String currentSection = null;

            while ((line = reader.readLine()) != null) {
                if (line.endsWith(":")) {
                    currentSection = line.substring(0, line.length() - 1);
                } else if ("Cidade".equals(currentSection)) {
                    processarLinhaCidade(line, cidades);
                } else if ("Rotas".equals(currentSection)) {
                    processarLinhaRota(line, rotas, cidades);
                }
            }
        } catch (IOException e) {
            return new CustomResponse(false, "Erro lendo o arquivo!");
        }

        if (cidades.isEmpty() && rotas.isEmpty()) {
            return new CustomResponse(false, "Arquivo vazio ou com formato incorreto!");
        }

        cidadeRepository.saveAll(cidades);
        rotaRepository.saveAll(rotas);

        return new CustomResponse(true, "Operação realizada com sucesso!");
    }

    private void processarLinhaCidade(String line, List<Cidade> cidades) {
        String[] parts = line.split(",");
        if (parts.length == 1) {
            String nome = parts[0].trim();
            cidades.add(new Cidade(nome));
        }
    }

    private void processarLinhaRota(String line, List<Rota> rotas, List<Cidade> cidades) {
        String[] parts = line.split(",");
        if (parts.length == 3) {
            String cidadeInicioS = parts[0].trim();
            String cidadeFimS = parts[1].trim();
            Double distancia = Double.parseDouble(parts[2].trim());
            
            Cidade cidadeInicio = getCidadePorNome(cidades, cidadeInicioS);
            Cidade cidadeFim = getCidadePorNome(cidades, cidadeFimS);
            
            rotas.add(new Rota(distancia, cidadeInicio, cidadeFim));
        }
    }
    
    private Cidade getCidadePorNome(List<Cidade> cidades, String nome) {
        for (Cidade cidade : cidades) {
            if (cidade.getNome().equals(nome)) {
                return cidade;
            }
        }
        return null;
    }

    @PostConstruct
    public void inicializar() throws FileNotFoundException, IOException {
        if (validationDB()) {
            postAll();
        }
    }

    public boolean validationDB() {
        return cidadeRepository.count() == 0 && rotaRepository.count() == 0;
    }

}