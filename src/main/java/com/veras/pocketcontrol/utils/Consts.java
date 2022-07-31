package com.veras.pocketcontrol.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Consts {
    public static final List<String> openPaths = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("/api/auth/login");
                add("/api/auth/user");
                add("/swagger-ui.html");

            }});
}
