package br.dev.marcosvirgilio.mobile.apostilaprog3app.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario {
    //atributos
    private int id;
    private String nome;
    private String email;
    private String senha;
    private boolean aceiteTermos;
    private String dataNascimento;
    private int sexo;
    private int perfil;
    //construtor
    //CONSTRUTOR - Inicializa os atributos para gerar Objeto Json
    public Usuario () {
        this.setId(0);
        this.setNome("");
        this.setSenha("");
        this.setEmail("");
        this.setAceiteTermos(false);
        this.setDataNascimento("1970-01-01");
        this.setSexo(0);
        this.setPerfil(0);
    }


    //CONSTRUTOR - inicializa atributos de um arquivo JSon
    public Usuario (JSONObject jp) {
        try {
            this.setId(jp.getInt("idUsuario"));
            this.setNome(jp.getString("nmUsuario"));
            this.setSenha(jp.getString("deSenha"));
            this.setEmail(jp.getString("deEmail"));
            this.setAceiteTermos(jp.getBoolean("opTermo"));
            this.setDataNascimento(jp.getString("dtNascimento"));
            this.setSexo(jp.getInt("cdSexo"));
            this.setPerfil(jp.getInt("idPerfil"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //Metodo retorna o objeto com dados no formato JSON
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("idUsuario", this.id);
            json.put("nmUsuario", this.nome);
            json.put("deSenha", this.senha);
            json.put("deEmail", this.email);
            json.put("opTermo", this.aceiteTermos);
            json.put("dtNascimento", this.dataNascimento);
            json.put("cdSexo", this.sexo);
            json.put("idPerfil", this.perfil);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    //métodos GET
    public boolean isAceiteTermos() {return aceiteTermos;}
    public String getDataNascimento() {return dataNascimento;}
    public int getSexo() {return sexo;}
    public int getPerfil() {return perfil;}
    public int getId() {return id;}
    public String getNome() {return nome;}
    public String getEmail() {return email;}
    public String getSenha() {return senha;}
    //métodos SET
    public void setDataNascimento(String dataNascimento) {
        //Verificando se o String recebido é uma data válida
        SimpleDateFormat formato =
                new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date data = (Date) formato.parse(dataNascimento);
            //se chegar até aqui não deu erro no parser
            this.dataNascimento = dataNascimento;
        } catch (ParseException e) {
            throw new IllegalArgumentException("Data inválida!");
        }
    }
    public void setId(int id) {
        //Verificando se o ID recebido é válido
        if (id > 0) {
            this.id = id;
        } else {
            throw new IllegalArgumentException(
                    "O ID deve ser maior que zero");
        }
    }
    public void setEmail(String email) {
        //Verificando se o email recebido é válido
        if (email.contains("@") && email.contains(".")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException(
                    "O email deve conter um @");
        }
    }
    public void setSenha(String senha) {
       /* Verificando se a senha recebida é válida
       Exluir letras (a-zA-Z), números (0-9) e espaços (\s).
       Sobram os caracteres especiais !@#$%^&*()_+={}[]:;"'<>,.?/|
       */
        String specialCharacters = "[^a-zA-Z0-9\\s]";
        if (senha.length() > 6 && senha.matches(".*" +
                specialCharacters + ".*")) {
            this.senha = senha;
        } else {
            throw new IllegalArgumentException(
                    "A senha deve caractere especial e 6 caracteres");
        }
    }
    public void setAceiteTermos(boolean aceiteTermos) {
        //Não precisa verificação sempre será true ou false
        this.aceiteTermos = aceiteTermos;
    }
    public void setSexo(int sexo) {
        //Verificando se o sexo recebido é válido
        if (sexo >= 0 || sexo <= 3) {
            this.sexo = sexo;
        } else {
            throw new IllegalArgumentException("Sexo Inválido");
        }
    }
    public void setPerfil(int perfil) {
        //Verificando se o perfil recebido é válido
        if (perfil >= 0 && perfil <=2) {
            this.perfil = perfil;
        } else {
            throw new IllegalArgumentException("Perfil Inválido!");
        }
    }
    public void setNome(String nome) {
       /*Verificando se o nome recebido é válido
       p{L} = Qualquer caracter Unicode (ã, ç, é, etc.)
       p{N} = Qualquer numero Unicode
       s = espaço em branco
       ^ e $ garantem que o string restrinja os caracteres*/
        String regex = "^[\\p{L}\\p{N}\\s]+$";
        if (nome.length() > 3 && nome.matches(regex)) {
            this.nome = nome;
        } else {
            throw new IllegalArgumentException(
                    "O nome não pode ter caracteres especiais!");
        }
    }

}
