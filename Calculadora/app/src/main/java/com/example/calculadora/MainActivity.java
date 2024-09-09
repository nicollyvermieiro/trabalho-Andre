package com.example.calculadora;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView visor;
    private String numeroAtual = "";
    private String numeroAnterior = "";
    private String operador = "";
    private boolean operadorPressionado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        visor = findViewById(R.id.textView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets barrasDoSistema = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barrasDoSistema.left, barrasDoSistema.top, barrasDoSistema.right, barrasDoSistema.bottom);
            return insets;
        });

        configurarBotoesNumericos();
        configurarBotoesOperadores();
    }

    private void configurarBotoesNumericos() {
        int[] botoesNumericos = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button botao = (Button) view;
                if (operadorPressionado) {
                    numeroAtual = "";
                    operadorPressionado = false;
                }
                numeroAtual += botao.getText().toString();
                visor.setText(numeroAtual);
            }
        };

        for (int id : botoesNumericos) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void configurarBotoesOperadores() {
        int[] botoesOperadores = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide, R.id.btnEquals, R.id.btnAC, R.id.btnInverter, R.id.btnPercent, R.id.btnDot};
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button botao = (Button) view;
                String textoBotao = botao.getText().toString();

                switch (textoBotao) {
                    case "AC":
                        limparCalculadora();
                        break;
                    case "+/-":
                        inverterSinal();
                        break;
                    case "%":
                        calcularPorcentagem();
                        break;
                    case ".":
                        adicionarPonto();
                        break;
                    case "=":
                        calcularResultado();
                        break;
                    default:
                        aplicarOperador(textoBotao);
                        break;
                }
            }
        };

        for (int id : botoesOperadores) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void limparCalculadora() {
        numeroAtual = "";
        numeroAnterior = "";
        operador = "";
        operadorPressionado = false;
        visor.setText("0");
    }

    private void inverterSinal() {
        if (!numeroAtual.isEmpty()) {
            if (numeroAtual.startsWith("-")) {
                numeroAtual = numeroAtual.substring(1);
            } else {
                numeroAtual = "-" + numeroAtual;
            }
            visor.setText(numeroAtual);
        }
    }

    private void calcularPorcentagem() {
        if (!numeroAtual.isEmpty()) {
            double valor = Double.parseDouble(numeroAtual) / 100;
            numeroAtual = String.valueOf(valor);
            visor.setText(numeroAtual);
        }
    }

    private void adicionarPonto() {
        if (!numeroAtual.contains(".")) {
            if (numeroAtual.isEmpty()) {
                numeroAtual = "0.";
            } else {
                numeroAtual += ".";
            }
            visor.setText(numeroAtual);
        }
    }

    private void aplicarOperador(String operadorEscolhido) {
        if (!numeroAtual.isEmpty()) {
            if (!operador.isEmpty()) {
                calcularResultado();
            }
            numeroAnterior = numeroAtual;
            operador = operadorEscolhido;
            operadorPressionado = true;
        }
    }

    private void calcularResultado() {
        if (!numeroAnterior.isEmpty() && !numeroAtual.isEmpty() && !operador.isEmpty()) {
            double resultado = 0;
            double primeiroNumero = Double.parseDouble(numeroAnterior);
            double segundoNumero = Double.parseDouble(numeroAtual);

            switch (operador) {
                case "+":
                    resultado = primeiroNumero + segundoNumero;
                    break;
                case "-":
                    resultado = primeiroNumero - segundoNumero;
                    break;
                case "*":
                    resultado = primeiroNumero * segundoNumero;
                    break;
                case "/":
                    if (segundoNumero != 0) {
                        resultado = primeiroNumero / segundoNumero;
                    } else {
                        visor.setText("Erro");
                        return;
                    }
                    break;
            }

            numeroAtual = String.valueOf(resultado);
            visor.setText(numeroAtual);
            operador = "";
            numeroAnterior = "";
            operadorPressionado = false;
        }
    }
}
