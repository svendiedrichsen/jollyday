package de.jollyday.parser.impl;

import de.jollyday.spi.Described;

import java.time.LocalDate;

/**
 * @author sdiedrichsen
 * @version $
 * @since 12.03.20
 */
class DescribedDateHolder {

	private LocalDate date;
	private Described described;

	public DescribedDateHolder(Described described, LocalDate date) {
		this.date = date;
		this.described = described;
	}

	public LocalDate getDate() {
		return date;
	}

	public Described getDescribed() {
		return described;
	}

}
