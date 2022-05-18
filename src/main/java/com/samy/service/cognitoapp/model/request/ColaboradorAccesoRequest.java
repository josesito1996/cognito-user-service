package com.samy.service.cognitoapp.model.request;

import static com.samy.service.cognitoapp.utils.Constant.REGEX_UUID;
import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ColaboradorAccesoRequest implements Serializable {

	private static final long serialVersionUID = 3794450219680234135L;

	@NotNull
	@NotEmpty
	@Pattern(regexp = REGEX_UUID)
	private String id;

	@Valid
	@NotNull
	@NotEmpty
	private List<AccesoRequest> accesos;

}
