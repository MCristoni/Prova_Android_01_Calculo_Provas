package matheuscristoni.calculoprovas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.txt_primeira_prova)
    EditText primeiraProva;

    @BindView(R.id.txt_segunda_prova)
    EditText segundaProva;

    @BindView(R.id.txt_peso)
    EditText peso;

    @BindView(R.id.txt_prova_especial)
    EditText especial;

    @BindView(R.id.txt_resultado)
    TextView resultado;

    @BindView(R.id.check_especial)
    CheckBox calcularEspecial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Mudar o campo de especial para usável apenas quando checkbox estiver marcado
        calcularEspecial.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    especial.setEnabled(true);
                    especial.setClickable(true);
                }else {
                    especial.setEnabled(false);
                    especial.setClickable(false);
                }
            }
        });
    }

    @OnClick(R.id.btn_calcular)
    public void calcular(){
        boolean fluxoCerto = true;
        int valorPrimeiraProva = 0;
        int valorSegundaProva = 0;
        int valorPesoProva = 0;
        int valorProvaEspecial = 0;

        //Consistências
        try {
            if (TextUtils.isEmpty(primeiraProva.getText().toString())){
                throw new Exception("Campo não pode estar vazio!");
            }
            valorPrimeiraProva = Integer.parseInt(primeiraProva.getText().toString());
            if (valorPrimeiraProva > 100 || valorPrimeiraProva < 0){
                throw new Exception("Valor inválido! Deve estar entre 0 e 100");
            }

            try {
                if (TextUtils.isEmpty(segundaProva.getText().toString())){
                    throw new Exception("Campo não pode estar vazio!");
                }
                valorSegundaProva = Integer.parseInt(segundaProva.getText().toString());
                if (valorSegundaProva > 100 || valorSegundaProva < 0){
                    throw new Exception("Valor inválido! Deve estar entre 0 e 100");
                }

                try {
                    if (TextUtils.isEmpty(peso.getText().toString())){
                        throw new Exception("Campo não pode estar vazio!");
                    }
                    valorPesoProva = Integer.parseInt(peso.getText().toString());
                    if (valorPesoProva > 4 || valorPesoProva < 1){
                        throw new Exception("Valor inválido! Deve estar entre 1 e 4");
                    }

                    if (calcularEspecial.isChecked()) {
                        try {
                            if (TextUtils.isEmpty(especial.getText().toString())){
                                throw new Exception("Campo não pode estar vazio!");
                            }
                            valorProvaEspecial = Integer.parseInt(especial.getText().toString());
                            if (valorProvaEspecial > 100 || valorProvaEspecial < 0) {
                                throw new Exception("Valor inválido! Deve estar entre 0 e 100");
                            }
                            fluxoCerto = true;
                        } catch (Exception e) {
                            fluxoCerto = false;
                            resultado.setText("");
                            especial.setError(e.getMessage());
                        }
                    }
                }catch (Exception e){
                    fluxoCerto = false;
                    resultado.setText("");
                    peso.setError(e.getMessage());
                }
            }catch (Exception e){
                fluxoCerto = false;
                resultado.setText("");
                segundaProva.setError(e.getMessage());
            }

        }catch (Exception e){
            fluxoCerto = false;
            resultado.setText("");
            primeiraProva.setError(e.getMessage());
        }

        if(fluxoCerto){
            String msg = "";
            int somas = 0;
            double mediaAritmetica = 0;

            try {
                somas = valorPrimeiraProva + (valorPesoProva*valorSegundaProva);
                mediaAritmetica = (somas/(valorPesoProva+1));
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //sem especial
            if (!calcularEspecial.isChecked()){
                if (mediaAritmetica >= 60){
                    msg = "Parabéns aluno aprovado!\nNota final: ";
                }
                else {
                    msg = "Que pena, você foi reprovado!\nNota final: ";
                }
                resultado.setText(msg + new DecimalFormat("0.00").format(mediaAritmetica));
            }
            //com especial
            else {
                double notaFinal = (mediaAritmetica + valorProvaEspecial)/2;
                if (notaFinal >= 60){
                    msg = "Parabéns aluno aprovado com a prova especial!\nNota final: ";
                }
                else {
                    msg = "Que pena, você foi reprovado com a prova especial!\nNota final: ";
                }
                resultado.setText(msg + new DecimalFormat("0.00").format(notaFinal));
            }
        }
    }
}
