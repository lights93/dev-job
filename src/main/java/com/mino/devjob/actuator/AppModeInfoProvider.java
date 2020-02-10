package com.mino.devjob.actuator;

import java.util.Random;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class AppModeInfoProvider implements InfoContributor {
	private final Random rnd = new Random();

	@Override
	public void contribute(Info.Builder builder) {
		boolean appMode = rnd.nextBoolean();
		builder.withDetail("application-mode", appMode ? "experimental" : "stable");
	}
}