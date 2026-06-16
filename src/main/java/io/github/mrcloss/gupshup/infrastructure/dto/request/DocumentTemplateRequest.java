package io.github.mrcloss.gupshup.infrastructure.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentTemplateRequest extends MediaTemplateRequest {}
