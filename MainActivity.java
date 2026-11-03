package mz.sasol.industry;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    int navy = Color.rgb(10, 29, 55), blue = Color.rgb(20, 75, 125);
    int white = Color.WHITE, green = Color.rgb(25, 145, 85), red = Color.rgb(190, 45, 45);
    double tempMax = 300, pressureMax = 100, gasMax = 10;
    LinearLayout root;
    EditText temperature, pressure, gas;

    public void onCreate(Bundle b) {
        super.onCreate(b);
        showLogin();
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s); t.setTextSize(size); t.setTextColor(white);
        t.setPadding(8, 12, 8, 12);
        return t;
    }

    void start(String title) {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(22, 28, 22, 22);
        root.setBackgroundColor(navy);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);
        TextView heading = text("SASOL INDUSTRY\n" + title, 23);
        heading.setTypeface(null, Typeface.BOLD);
        heading.setGravity(Gravity.CENTER);
        root.addView(heading);
        View line = new View(this);
        line.setBackgroundColor(blue);
        root.addView(line, new LinearLayout.LayoutParams(-1, 3));
    }

    EditText field(String hint, String value) {
        EditText e = new EditText(this);
        e.setSingleLine(true); e.setHint(hint); e.setText(value);
        e.setTextColor(white); e.setHintTextColor(Color.LTGRAY);
        root.addView(e);
        return e;
    }

    void button(String label, Runnable action) {
        Button b = new Button(this);
        b.setText(label); b.setTextColor(white); b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(blue));
        root.addView(b, new LinearLayout.LayoutParams(-1, -2));
        b.setOnClickListener(v -> action.run());
    }

    void showLogin() {
        start("SISTEMA DE SEGURANÇA INDUSTRIAL");
        root.addView(text("Acesso ao sistema", 18));
        EditText pass = field("Palavra-passe", "");
        pass.setInputType(129);
        button("ENTRAR", () -> {
            if (pass.getText().toString().equals("201300")) showMenu();
            else Toast.makeText(this, "Palavra-passe incorreta", Toast.LENGTH_SHORT).show();
        });
        root.addView(text("Versão demonstrativa — altere a palavra-passe antes de uso real.", 12));
    }

    void showMenu() {
        start("PAINEL PRINCIPAL");
        button("Equipamentos da indústria", () -> showEquipment());
        button("Monitorização e diagnóstico", () -> showDiagnosis());
        button("Alterar limites", () -> showLimits());
        button("Sair", () -> showLogin());
    }

    void showEquipment() {
        start("EQUIPAMENTOS");
        root.addView(text("FORNO INDUSTRIAL\nAquece substâncias e processos industriais.", 17));
        root.addView(text("COMPRESSOR\nComprime gases e mantém a pressão do processo.", 17));
        root.addView(text("TANQUE / DETETOR DE GÁS\nArmazena substâncias; a concentração de gás deve ser monitorizada por sensor apropriado.", 17));
        button("Voltar", () -> showMenu());
    }

    void showDiagnosis() {
        start("MONITORIZAÇÃO E DIAGNÓSTICO");
        temperature = field("Temperatura do forno (°C)", "");
        pressure = field("Pressão do compressor (bar)", "");
        gas = field("Concentração de gás (% LEL)", "");
        TextView report = text("Introduza os valores e selecione DIAGNOSTICAR.", 16);
        root.addView(report);
        button("DIAGNOSTICAR", () -> {
            try {
                double t = Double.parseDouble(temperature.getText().toString());
                double p = Double.parseDouble(pressure.getText().toString());
                double g = Double.parseDouble(gas.getText().toString());
                int danger = 0, normal = 0;
                StringBuilder r = new StringBuilder("RELATÓRIO GERAL\n\n");
                if (t > tempMax) { r.append("FORNO: PERIGO — temperatura acima do limite. Pare/avalie conforme procedimento industrial e verifique o sistema de aquecimento.\n\n"); danger++; }
                else { r.append("FORNO: NORMAL\n\n"); normal++; }
                if (p > pressureMax) { r.append("COMPRESSOR: PERIGO — pressão acima do limite. Siga o procedimento de segurança e solicite verificação de válvulas/tubagens.\n\n"); danger++; }
                else { r.append("COMPRESSOR: NORMAL\n\n"); normal++; }
                if (g >= gasMax) { r.append("GÁS: PERIGO — concentração atingiu o limite. Afaste-se e siga o protocolo de emergência da instalação.\n\n"); danger++; }
                else { r.append("GÁS: NORMAL\n\n"); normal++; }
                r.append("Equipamentos normais: ").append(normal).append("\nEm risco: ").append(danger);
                r.append(danger > 0 ? "\n\nCONCLUSÃO: RISCO DETETADO." : "\n\nCONCLUSÃO: Sem risco indicado pelos valores inseridos.");
                report.setText(r.toString());
                report.setBackgroundColor(danger > 0 ? red : green);
            } catch (Exception ex) {
                Toast.makeText(this, "Introduza números válidos nos três campos.", Toast.LENGTH_LONG).show();
            }
        });
        button("Voltar", () -> showMenu());
    }

    void showLimits() {
        start("CONFIGURAÇÃO DOS LIMITES");
        EditText t = field("Temperatura máxima (°C)", String.valueOf(tempMax));
        EditText p = field("Pressão máxima (bar)", String.valueOf(pressureMax));
        EditText g = field("Gás máximo (% LEL)", String.valueOf(gasMax));
        button("GUARDAR LIMITES", () -> {
            try {
                double nt = Double.parseDouble(t.getText().toString());
                double np = Double.parseDouble(p.getText().toString());
                double ng = Double.parseDouble(g.getText().toString());
                if (nt <= 0 || np <= 0 || ng <= 0) throw new Exception();
                tempMax = nt; pressureMax = np; gasMax = ng;
                Toast.makeText(this, "Limites atualizados.", Toast.LENGTH_SHORT).show();
                showMenu();
            } catch (Exception e) {
                Toast.makeText(this, "Todos os limites devem ser números positivos.", Toast.LENGTH_LONG).show();
            }
        });
        button("Voltar", () -> showMenu());
    }
}
