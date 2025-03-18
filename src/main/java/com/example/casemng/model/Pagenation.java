package com.example.casemng.model;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

@Component
public class Pagenation {

	public Pageable getPageable(Integer displayedNum, String sortKey, String sortDirection,
			Pageable pageable, Model model) {

		Sort sort = null;
		if (StringUtils.hasLength(sortDirection)) {
			String sd = sortDirection.equals(Sort.Direction.ASC.name()) ? Sort.Direction.ASC.name()
					: Sort.Direction.DESC.name();
			String si = sortKey;
			model.addAttribute("sortKey", si);

			sort = Sort.by(Sort.Direction.fromString(sd), si);
			model.addAttribute("sortDirection", sd);
		} else {
			sort = Sort.by(Sort.Direction.ASC, "id");
		}
		if (displayedNum == null) {
			displayedNum = 5;
		}
		Pageable p = sort == null ? pageable
				: PageRequest.of(pageable.getPageNumber() > 0 ? pageable.getPageNumber() - 1 : pageable.getPageNumber(),
						displayedNum, sort);
		return p;
	}
}
