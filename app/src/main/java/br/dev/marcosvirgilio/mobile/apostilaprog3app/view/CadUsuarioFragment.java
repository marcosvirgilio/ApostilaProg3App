package br.dev.marcosvirgilio.mobile.apostilaprog3app.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.dev.marcosvirgilio.mobile.apostilaprog3app.R;
import br.dev.marcosvirgilio.mobile.apostilaprog3app.model.Perfil;
import br.dev.marcosvirgilio.mobile.apostilaprog3app.model.Usuario;

public class CadUsuarioFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RequestQueue requestQueue;
    private Spinner spPerfis;
    private EditText etNome;
    private EditText etMail;
    private EditText etSenha;
    private CheckBox cbAceite;
    private RadioGroup rgSexo;
    private CalendarView cvDataNasc;
    private Button btSalvar;


    public CadUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_cad_usuario, container, false);
        //spinner
        this.spPerfis = (Spinner) view.findViewById(R.id.spPerfis);

        this.etNome = (EditText) view.findViewById(R.id.etNome);
        this.etMail = (EditText) view.findViewById(R.id.etMail);
        this.etSenha = (EditText) view.findViewById(R.id.etSenha);
        //radio group
        this.rgSexo = (RadioGroup) view.findViewById(R.id.rgSexo);
        //checkBox
        this.cbAceite = (CheckBox) view.findViewById(R.id.cbAceite);
        //calendarView
        this.cvDataNasc = (CalendarView)
                view.findViewById(R.id.cvDataNasc);
        this.btSalvar = (Button) view.findViewById(R.id.btSalvar);
        //
        //definindo o listener do botão
        this.btSalvar.setOnClickListener(this);
        //instanciando a fila de requests - caso o objeto seja o view
        this.requestQueue = Volley.newRequestQueue(view.getContext());
//inicializando a fila de requests do SO
        this.requestQueue.start();
        //consultar
        try {
            this.consultaPerfis();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //return defoult
        return this.view;
    }

    private void cadastrarUsuario(Usuario u) throws JSONException {
        //requisição para o Rest Server
        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(
                Request.Method.POST,
                "http://10.0.2.2:8080/seg/cadusuario.php",
                u.toJsonObject(),
                response -> {
                    try {
                        //se a consulta não veio vazia
                        if (response != null) {
                            Context context = requireContext();
                            if (response.getBoolean("success")) {
                                //limpar campos da tela
                                this.etNome.setText("");
                                this.etMail.setText("");
                                this.etSenha.setText("");
                                //primeiro item dos spinners
                                this.spPerfis.setSelection(0);
                            }
                            //mostrando a mensagem que veio do JSON
                            Toast toast = Toast.makeText(
                                    view.getContext(),
                                    response.getString("message"),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        } else {
                            Snackbar mensagem = Snackbar.make(view,
                                    "A consulta não retornou nada!",
                                    Snackbar.LENGTH_LONG);
                            mensagem.show();
                        }
                    } catch (Exception e) {
                        Snackbar mensagem = Snackbar.make(view,
                                "Ops!Problema com o arquivo JSON: " +
                                        e, Snackbar.LENGTH_LONG);
                        mensagem.show();
                    }
                },
                error -> {
                    //mostrar mensagem que veio do servidor
                    Snackbar mensagem = Snackbar.make(view,
                            "Ops! Houve um problema: " +
                                    error.toString(), Snackbar.LENGTH_LONG);
                    mensagem.show();
                }
        );
        //colocando nova request para fila de execução
        requestQueue.add(jsonObjectReq);
    }

    private void consultaPerfis() throws JSONException {
        //requisição para o Rest Server
        JsonArrayRequest jsonArrayReq = null;
        try {
            jsonArrayReq = new JsonArrayRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8080/seg/conperfis.php",
                    new JSONArray("[{}]"),
                    response -> {
                        try {
                            //se a consulta não veio vazia
                            if (response != null) {
                                //array list para receber a resposta
                                ArrayList<Perfil> listaPerfis = new ArrayList<>();
                                //preenchendo ArrayList com JSONArray recebido
                                for (int pos = 0; pos <= response.length(); pos++) {
                                    JSONObject jo = response.getJSONObject(pos);
                                    Perfil perfil = new Perfil();
                                    perfil.setIdPerfil(jo.getInt("idPerfil"));
                                    perfil.setNmPerfil(jo.getString("nmPerfil"));
                                    listaPerfis.add(pos, perfil);
                                }
                                //passando array list para atualizar itens do spinner de perfil
                                ArrayAdapter<Perfil> adapter = new ArrayAdapter<>(
                                        requireContext(),
                                        android.R.layout.simple_spinner_item,
                                        listaPerfis);

                                adapter.addAll(listaPerfis);
                                //colocando lista no spinner
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spPerfis.setAdapter(adapter);
                            } else {
                                Snackbar mensagem = Snackbar.make(view,
                                        "A consulta não retornou nenhum registro!",
                                        Snackbar.LENGTH_LONG);
                                mensagem.show();
                            }
                        } catch (JSONException e) {
                            Snackbar mensagem = Snackbar.make(view,
                                    "Ops!Problema com o arquivo JSON: " +
                                            e, Snackbar.LENGTH_LONG);
                            mensagem.show();
                        }
                    },
                    error -> {
                        //mostrar mensagem que veio do servidor
                        Snackbar mensagem = Snackbar.make(view,
                                "Ops! Houve um problema ao realizar a consulta: " +
                                        error.toString(), Snackbar.LENGTH_LONG);
                        mensagem.show();
                    }
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        //colocando nova request para fila de execução
        requestQueue.add(jsonArrayReq);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btSalvar) {
            //instanciando objeto de negócio
            Usuario u = new Usuario();
            //populando objeto com dados da tela
            u.setNome(this.etNome.getText().toString());
            u.setEmail(this.etMail.getText().toString());
            u.setSenha(this.etSenha.getText().toString());
            //indice radio group button selecionado
            int radioButtonID =
                    rgSexo.getCheckedRadioButtonId();
            View radioButton =
                    rgSexo.findViewById(radioButtonID);
            u.setSexo(rgSexo.indexOfChild(radioButton));
            //status do checkBox
            u.setAceiteTermos(this.cbAceite.isChecked());
            //indice do item selecionado do Spinner
            int posPerfil = spPerfis.getSelectedItemPosition();
            Perfil perfil = (Perfil) spPerfis.getItemAtPosition(posPerfil);
            u.setPerfil(perfil.getIdPerfil());
            //Pegando a Data do CalendarView
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataSelecionada = sdf.format(new
                    Date(cvDataNasc.getDate()));
            u.setDataNascimento(dataSelecionada);
            //chamada do web service de cadastro
            try {
                cadastrarUsuario(u);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

    }
}