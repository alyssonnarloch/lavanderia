package com.lavanderia.util;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Validation {

	@Autowired
	protected SessionFactory sessionFactory;

	public boolean isUnique(int id, String className, String value, String field) {
		Session session = this.sessionFactory.openSession();

		int rows = 0;

		Query query = session.createQuery("SELECT COUNT(id) FROM " + className + " WHERE " + field + " = '" + value + "' AND id <> " + id);

		List listResult = (List) query.list();
		Number number = (Number) listResult.get(0);
		rows = number.intValue();

		session.close();

		return rows == 0;
	}

	public boolean validDate(Date date) {
		try {
			date.getTime();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean validEmail(String email) {
		Pattern pattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public boolean validCpf(String strCpf) {
		strCpf = strCpf.replaceAll("[^0-9]", "");

		int d1, d2;
		int digito1, digito2, resto;
		int digitoCPF;
		String nDigResult;

		d1 = d2 = 0;
		digito1 = digito2 = resto = 0;

		for (int nCount = 1; nCount < strCpf.length() - 1; nCount++) {
			digitoCPF = Integer.valueOf(strCpf.substring(nCount - 1, nCount))
					.intValue();

			d1 = d1 + (11 - nCount) * digitoCPF;

			d2 = d2 + (12 - nCount) * digitoCPF;
		}
		;

		resto = (d1 % 11);

		if (resto < 2)
			digito1 = 0;
		else
			digito1 = 11 - resto;

		d2 += 2 * digito1;

		resto = (d2 % 11);

		if (resto < 2)
			digito2 = 0;
		else
			digito2 = 11 - resto;

		String nDigVerific = strCpf.substring(strCpf.length() - 2,
				strCpf.length());

		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

		return nDigVerific.equals(nDigResult);
	}

	public boolean validLength(String str, int validLength) {
		return str.length() <= validLength;
	}
}
