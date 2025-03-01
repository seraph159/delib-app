package com.delibrary.lib_backend.service_librarian;

import com.delibrary.lib_backend.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
