package com.mediconnect.service;

import com.mediconnect.dto.request.DelegationRequest;
import com.mediconnect.dto.response.DelegationResponse;
import java.util.List;

public interface DelegationService {
    DelegationResponse createDelegation(Long doctorId, DelegationRequest request);
    List<DelegationResponse> getActiveDelegations(Long doctorId);
    DelegationResponse revokeDelegation(Long delegationId);
}