package com.onlinemed.controllers;

import com.blueveery.scopes.JsonScope;
import com.onlinemed.model.Security;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class used to define methods that operate on objects of class Security
 */
@RestController
@RequestMapping("/api/security")
@JsonScope(positive = true, scope = {Security.class})
public class SecurityCtrl {

}
