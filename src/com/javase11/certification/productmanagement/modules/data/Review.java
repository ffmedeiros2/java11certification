/*
 * Copyright (c) 2021. Oracle
 *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at  your option) any later version.
 *
 *  This program is distributed in the hope it will be useful, but WITHOUT ANY WARRANTY: without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License along with this program. If not see <http://www.gnu.org/licenses>
 */

package com.javase11.certification.productmanagement.modules.data;

import java.io.Serializable;

public class Review implements Comparable<Review>, Serializable {
	private static final long serialVersionUID = -4606606218242108290L;
	private final Rating rating;
	private final String comments;

	public Review(final Rating rating, final String comments) {
		this.rating = rating;
		this.comments = comments;
	}

	public Rating getRating() {
		return rating;
	}

	public String getComments() {
		return comments;
	}

	@Override
	public String toString() {
		return "Review{" + "rating=" + rating + ", comments='" + comments + '\'' + '}';
	}

	@Override
	public int compareTo(final Review other) {
		return other.rating.ordinal() - this.rating.ordinal();
	}
}
