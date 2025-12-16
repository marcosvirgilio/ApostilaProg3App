package br.dev.marcosvirgilio.mobile.apostilaprog3app.view;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import br.dev.marcosvirgilio.mobile.apostilaprog3app.R;
import br.dev.marcosvirgilio.mobile.apostilaprog3app.model.Usuario;

/**
 * A fragment representing a list of Items.
 */
public class ConUsuarioFragment extends Fragment {
    private final int mColumnCount = 1;
    //Fila de requests da biblioteca Volley
    private RequestQueue requestQueue;
    //Objeto view que representa a tela utilizado em diversos metodos
    private View view;

    public ConUsuarioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_con_usuario_list, container, false);

        //instanciando a fila de requests - caso o objeto seja o view
        this.requestQueue = Volley.newRequestQueue(view.getContext());
        //inicializando a fila de requests do SO
        this.requestQueue.start();
        return view;
    }

    private void consultaUsuarios() {
        //requisição para o Rest Server
        JsonArrayRequest jsonArrayReq = null;
        try {
            jsonArrayReq = new JsonArrayRequest(
                    Request.Method.POST,
                    "http://10.0.2.2/seg/conusuario.php",
                    new JSONArray("[{}]"),
                    response -> {
                        try {
                            //se a consulta não veio vazia passar para array list
                            if (response != null) {
                                //objeto java
                                Usuario usuario = null;
                                //array list para receber a resposta
                                ArrayList<Usuario> arrayList= new ArrayList<Usuario>();
                                //preenchendo ArrayList com JSONArray recebido
                                for (int pos=0; pos<response.length();pos++) {
                                    JSONObject jo = response.getJSONObject(pos);
                                    usuario = new Usuario(jo);
                                    arrayList.add(usuario);
                                }
                                //O código abaixo era originalmente do metodo onCreateView()
                                if (this.view instanceof RecyclerView) {
                                    Context context = view.getContext();
                                    RecyclerView recyclerView = (RecyclerView) view;
                                    if (mColumnCount <= 1) {
                                        recyclerView.setLayoutManager(
                                                new LinearLayoutManager(context));
                                    } else {
                                        recyclerView.setLayoutManager(
                                                new GridLayoutManager(context, mColumnCount));
                                    }
                                    recyclerView.setAdapter(
                                            new ConUsuarioRecyclerViewAdapter(arrayList));
                                }
                            }else {
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
}