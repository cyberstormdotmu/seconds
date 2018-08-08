package com.ishoal.ws.admin.controller;


import com.ishoal.core.domain.OfferReference;
import com.ishoal.core.domain.OfferReport;
import com.ishoal.core.offer.OfferReportService;
import com.ishoal.ws.admin.dto.AdminOfferReportDto;
import com.ishoal.ws.admin.dto.adapter.AdminOfferReportDtoAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/admin/offers/")
public class AdminOfferReportController {
    private static final Logger logger = LoggerFactory.getLogger(AdminOfferReportController.class);

    private final OfferReportService offerReportService;
    private final AdminOfferReportDtoAdapter reportAdapter = new AdminOfferReportDtoAdapter();

    public AdminOfferReportController(OfferReportService offerReportService) {
        this.offerReportService = offerReportService;
    }

    @RequestMapping(method = RequestMethod.GET, value="{offerReference}/report")
    public ResponseEntity<AdminOfferReportDto> createOfferReport(@PathVariable("offerReference") String offerReference) {
        logger.info("createOfferReport for offer with reference [{}]", offerReference);

        OfferReport report = offerReportService.createOfferReport(OfferReference.from(offerReference));

        return ResponseEntity.ok(reportAdapter.adapt(report));
    }
}
