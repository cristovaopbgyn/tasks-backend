package br.ce.wcaquino.taskbackend.utils;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilsTest {
	
	@Test
	public void retornaTrueParaDataFutura() {
		LocalDate data = LocalDate.of(2090, 1, 1);
		Assert.assertFalse(DateUtils.isEqualOrFutureDate(data));
	}
	
	@Test
	public void retornaFalseParaDataPassada() {
		LocalDate data = LocalDate.of(2014, 1, 1);
		Assert.assertFalse(DateUtils.isEqualOrFutureDate(data));
	}
	
	@Test
	public void retornaTrueParaDataPresente() {
		LocalDate data = LocalDate.now();
		Assert.assertTrue(DateUtils.isEqualOrFutureDate(data));
		
		System.out.println("teste");
	}
}
