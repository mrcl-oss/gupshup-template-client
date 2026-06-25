package io.github.mrcloss.gupshup.infrastructure.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/** Response returned when a template is successfully deleted. */
@Getter
@Setter
@ToString(callSuper = true)
public class DeleteTemplateResponse extends BaseGupshupResponse {}
