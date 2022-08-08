package com.veras.pocketcontrol.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Consts {
    public static final List<String> OPEN_PATHS = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("/api/auth/login");
                add("/api/auth/user");
                add("/swagger-ui.html");
            }}
    );

    public static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado";
    public static final String USER_CREATED_MESSAGE = "Usuário criado com sucesso!";
    public static final String NO_TRANSACTIONS_TO_INSERT_MESSAGE = "Nenhuma transação a ser cadastrada";
}
