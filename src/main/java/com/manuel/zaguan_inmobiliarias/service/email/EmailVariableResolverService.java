package com.manuel.zaguan_inmobiliarias.service.email;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailVariableResolverService {

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{\\{\\s*(\\w+)\\s*\\}\\}");

    public String resolve(String template, Map<String, String> variables) {
        return resolve(template, variables, UnaryOperator.identity());
    }

    //Para cuerpos HTML: los valores los escribe un usuario y no se pueden colar como etiquetas
    public String resolveHtml(String template, Map<String, String> variables) {
        return resolve(template, variables, HtmlUtils::htmlEscape);
    }

    private String resolve(String template, Map<String, String> variables, UnaryOperator<String> encoder) {
        Matcher matcher = VARIABLE_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String value = encoder.apply(variables.getOrDefault(variableName, ""));
            matcher.appendReplacement(result, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(result);

        return result.toString();
    }
}