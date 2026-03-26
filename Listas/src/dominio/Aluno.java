package src.dominio;

public class Aluno {

    private String nome;
    private int matricula;
    private int nota;
      
    public Aluno(int matricula ,String nome, int nota){
        this.nome = nome;
        this.matricula = matricula;
        this.nota = nota;
    }
    
    
    @Override
    public boolean equals(Object a){
        if (this == a) return true;
        if (a == null || getClass() != a.getClass()) return false;
        Aluno aluno = (Aluno) a;
        return this.matricula == aluno.matricula;
    }
    
    @Override
    public String toString(){
        return "Matricula: " + matricula + " | Nome: " + nome + " | Nota: " + nota;
    }


    /**
     * Cria um objeto-chave para buscas e remoções por matrícula.
     * Utilizado em conjunto com equals(), que compara apenas a matrícula.
     */
    public static Aluno porMatricula(int matricula) {
        return new Aluno(matricula, "", 0);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}