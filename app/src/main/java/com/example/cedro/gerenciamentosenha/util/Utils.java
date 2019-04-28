package com.example.cedro.gerenciamentosenha.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.cedro.gerenciamentosenha.R;
import com.example.cedro.gerenciamentosenha.model.Site;
import com.example.cedro.gerenciamentosenha.model.Usuario;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 10) {
            return false;
        }
        String regExpn =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";

        CharSequence inputStr = password;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static Site recuperaDadosUsuario(Context context){
        Site result = new Site();
        Usuario usuario = new Usuario();
        SharedPreferences sharedPreferences;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.usuario), Context.MODE_PRIVATE);
        result.setToken(sharedPreferences.getString(context.getString(R.string.token), ""));

        usuario.setEmail(sharedPreferences.getString(context.getString(R.string.usuario), ""));
        usuario.setSenha(sharedPreferences.getString(context.getString(R.string.senha), ""));
        result.setUsuario (usuario);
        return result;
    }
}
