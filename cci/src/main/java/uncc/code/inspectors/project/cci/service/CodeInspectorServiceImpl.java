package uncc.code.inspectors.project.cci.service;

import uncc.code.inspectors.project.cci.entity.Application;
import uncc.code.inspectors.project.cci.entity.CodeInspector;
import uncc.code.inspectors.project.cci.entity.Pagination;
import uncc.code.inspectors.project.cci.repository.CodeInspectorRepository;
import uncc.code.inspectors.request.CodeInspectorRequest;

import java.time.ZoneId;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class CodeInspectorServiceImpl implements CodeInspectorService {

    @Autowired
    public CodeInspectorRepository codeInspectorRepository;

    public Pagination setPagination(Page<CodeInspector> results, int startPage, int size) {
        Pagination pagination = new Pagination();
        pagination.setCurrentPage(results.getNumber());
        pagination.setStartPage(startPage);
        pagination.setRecordsPerPage(size);
        pagination.setLastPage(results.getTotalPages() - 1);
        pagination.setTotalRecords(results.getTotalElements());
        return pagination;
    }

    public Page<CodeInspector> findByField(CodeInspectorRequest codeInspectorRequest, int page, int size) {
        final Long ceoId = (codeInspectorRequest == null) ? null : codeInspectorRequest.getCeoId();
        return null;
    }

    // get code inspectors
    @Override
    public List<CodeInspector> getCodeInspectors(CodeInspectorRequest codeInspectorRequest) {
        // TODO: Filter with a Mongo query. Not a huge issue since the # of records is small.
        List<CodeInspector> inspectors = new ArrayList<>();

        final Long ceoId = (codeInspectorRequest == null) ? null : codeInspectorRequest.getCeoId();
        final Integer level = (codeInspectorRequest == null) ? null : codeInspectorRequest.getLevel();
        final String lastName = (codeInspectorRequest == null) ? null : codeInspectorRequest.getLastName();
        final String trade = (codeInspectorRequest == null) ? null : codeInspectorRequest.getTrade();
        final String type = (codeInspectorRequest == null) ? null : codeInspectorRequest.getType();
        final String employer = (codeInspectorRequest == null) ? null : codeInspectorRequest.getEmployer();
        final String county = (codeInspectorRequest == null) ? null : codeInspectorRequest.getCounty();
        final Integer zipCode = (codeInspectorRequest == null) ? null : codeInspectorRequest.getZipCode();

        for (CodeInspector inspector : codeInspectorRepository.findAll()) {
            if (ceoId != null && !inspector.getCeoId().equals(ceoId)) {
                continue;
            }
            if (level != null && !inspector.getLevel().equals(level)) {
                continue;
            }
            if (lastName != null && !inspector.getLastName().equals(lastName)) {
                continue;
            }
            if (trade != null && !inspector.getTrade().equals(trade)) {
                continue;
            }
            if (type != null && !inspector.getType().equals(type)) {
                continue;
            }
            if (employer != null && !inspector.getEmployer().equals(employer)) {
                continue;
            }
            if (county != null && !inspector.getCounty().equals(county)) {
                continue;
            }
            if (zipCode != null && (inspector.getZipCode() == null || !inspector.getZipCode().equals(zipCode))) {
                continue;
            }
            inspectors.add(inspector);
        }
        return withoutPrivateData(inspectors);
    }

    // get single code inspectors
    @Override
    public CodeInspector getACodeInspector(String id) {
        return withoutPrivateData(codeInspectorRepository.findById(id).orElse(null));
    }

    @Override
    public CodeInspector createCodeInspector(CodeInspector codeInspector) {
        // Required fields: username, password, firstName, lastName,
        // phone, email, level, type, certificationNum, ceoId
        if (codeInspector.getUsername() == null || codeInspector.getPassword() == null
                || codeInspector.getFirstName() == null || codeInspector.getLastName() == null
                || codeInspector.getPhone() == null || codeInspector.getEmail() == null
                || codeInspector.getLevel() == null || codeInspector.getType() == null
                || codeInspector.getCertificationNum() == null || codeInspector.getCeoId() == null) {
            return null;
        }

        // If a user with the same username exists, return null
        if (codeInspectorRepository.findByUsername(codeInspector.getUsername()) != null) {
            return null;
        }

        CodeInspector newCodeInspector = new CodeInspector();

        newCodeInspector.setCeoId(codeInspector.getCeoId());
        newCodeInspector.setCertificationNum(codeInspector.getCertificationNum());
        newCodeInspector.setLevel(codeInspector.getLevel());
        newCodeInspector.setFirstName(codeInspector.getFirstName());
        newCodeInspector.setLastName(codeInspector.getLastName());
        newCodeInspector.setEmail(codeInspector.getEmail());
        newCodeInspector.setPhone(codeInspector.getPhone());
        newCodeInspector.setTrade(codeInspector.getTrade());
        newCodeInspector.setType(codeInspector.getType());
        newCodeInspector.setEmployer(codeInspector.getEmployer());
        newCodeInspector.setExpirationDate(codeInspector.getExpirationDate());
        newCodeInspector.setAddress(codeInspector.getAddress());
        newCodeInspector.setCity(codeInspector.getCity());
        newCodeInspector.setState(codeInspector.getState());
        newCodeInspector.setZipCode(codeInspector.getZipCode());
        newCodeInspector.setCounty(codeInspector.getCounty());
        newCodeInspector.setUsername(codeInspector.getUsername());
        setPassword(newCodeInspector, codeInspector.getPassword());
        codeInspectorRepository.save(newCodeInspector);
        return withoutSensitiveData(newCodeInspector);
    }

    // delete an object
    @Override
    public void deleteCodeInspector(Long id, String cerNum, String firstName, String lastName) {
        CodeInspector codeInspector = codeInspectorRepository.findByCeoIdAndCertificationNumAndFirstNameAndLastName(id, cerNum, firstName, lastName);
        codeInspectorRepository.delete(codeInspector);
    }

    // update object
    @Override
    public CodeInspector updateCodeInspector(CodeInspector codeInspector) {
        CodeInspector updatedInspector = codeInspectorRepository.findByCeoIdAndCertificationNumAndFirstNameAndLastName(codeInspector.getCeoId(), codeInspector.getCertificationNum(), codeInspector.getFirstName(), codeInspector.getLastName());

        if (codeInspector.getLevel() != null) {
            updatedInspector.setLevel(codeInspector.getLevel());
        }
        if (codeInspector.getEmail() != null) {
            updatedInspector.setEmail(codeInspector.getEmail());
        }
        if (codeInspector.getPhone() != null) {
            updatedInspector.setPhone(codeInspector.getPhone());
        }
        if (codeInspector.getTrade() != null) {
            updatedInspector.setTrade(codeInspector.getTrade());
        }
        if (codeInspector.getType() != null) {
            updatedInspector.setType(codeInspector.getType());
        }
        if (codeInspector.getEmployer() != null) {
            updatedInspector.setEmployer(codeInspector.getEmployer());
        }
        if (codeInspector.getExpirationDate() != null) {
            updatedInspector.setExpirationDate(codeInspector.getExpirationDate());
        }
        if (codeInspector.getAddress() != null) {
            updatedInspector.setAddress(codeInspector.getAddress());
        }
        if (codeInspector.getCity() != null) {
            updatedInspector.setCity(codeInspector.getCity());
        }
        if (codeInspector.getState() != null) {
            updatedInspector.setState(codeInspector.getState());
        }
        if (codeInspector.getZipCode() != null) {
            updatedInspector.setZipCode(codeInspector.getZipCode());
        }
        if (codeInspector.getCounty() != null) {
            updatedInspector.setCounty(codeInspector.getCounty());
        }
        if (codeInspector.getUsername() != null) {
            updatedInspector.setUsername(codeInspector.getUsername());
        }
        if (codeInspector.getPassword() != null) {
            setPassword(updatedInspector, codeInspector.getPassword());
        }

        return withoutPrivateData(codeInspectorRepository.save(updatedInspector));
    }

    // login
    @Override
    public CodeInspector login(String username, String password) {
        password += "code-inspector-salt";
        try {
            var md = java.security.MessageDigest.getInstance("SHA256");
            password = Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return withoutSensitiveData(codeInspectorRepository.findByUsernameAndPassword(username, password));
    }

    @Override
    public CodeInspector scheduleInspection(Application application) {
        // Find the inspector with the given id
        CodeInspector codeInspector = codeInspectorRepository.findById(application.getId()).orElse(null);
        boolean success = false;
        if (codeInspector != null) {
            // Find a slot with the given time
            var slots = codeInspector.getSlots();
            if (slots == null) {
                slots = new ArrayList<>();
            }
            for (int i = 0; i < slots.size(); i++) {
                var slot = slots.get(i);
                // If the slot has an approved application, skip it
                if (slot.getApprovedApplication() != null) {
                    continue;
                }

                // TODO: Remove debug printing here
                System.out.println(slot.getStartTime());
                System.out.println(application.getTime());
                System.out.println(slot.getStartTime().equals(application.getTime()));

                if (slot.getStartTime().equals(application.getTime())) {
                    // Add the application to the pending applications
                    var pendingApplications = slot.getPendingApplications();
                    if (pendingApplications == null) {
                        pendingApplications = new ArrayList<>();
                    }
                    pendingApplications.add(application);
                    slot.setPendingApplications(pendingApplications);
                    slots.set(i, slot);
                    success = true;
                    break;
                }
            }
            if (success) {
                codeInspector.setSlots(slots);
                codeInspectorRepository.save(codeInspector);
            }
        }
        if (success) {
            return withoutPrivateData(codeInspector);
        } else {
            return null;
        }
    }

    private static void setPassword(CodeInspector codeInspector, String password) {
        if (password != null) {
            password += "code-inspector-salt";
            try {
                var md = java.security.MessageDigest.getInstance("SHA256");
                password = Base64.getEncoder().encodeToString(md.digest(password.getBytes()));
                codeInspector.setPassword(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Removes sensitive data from the given code inspector. This is data that should never be returned to the client.
     *
     * @param codeInspector The code inspector to remove sensitive data from.
     * @return The code inspector without sensitive data.
     */
    private static CodeInspector withoutSensitiveData(CodeInspector codeInspector) {
        if (codeInspector != null) {
            codeInspector.setPassword(null);
            codeInspector.setUsername(null);
        }
        return codeInspector;
    }

    private static List<CodeInspector> withoutSensitiveData(List<CodeInspector> codeInspectors) {
        codeInspectors.replaceAll(CodeInspectorServiceImpl::withoutSensitiveData);
        return codeInspectors;
    }

    /**
     * Removes private data from the given code inspector. This is data that should only be returned to the client if
     * they are the code inspector.
     */
    private static CodeInspector withoutPrivateData(CodeInspector codeInspector) {
        if (codeInspector != null) {
            codeInspector.setPassword(null);
            codeInspector.setUsername(null);
            var slots = codeInspector.getSlots();
            if (slots != null) {
                slots.forEach(slot -> {
                    slot.setPendingApplications(null);
                    slot.setApprovedApplication(null);
                });
            }
        }
        return codeInspector;
    }

    private static List<CodeInspector> withoutPrivateData(List<CodeInspector> codeInspectors) {
        codeInspectors.replaceAll(CodeInspectorServiceImpl::withoutPrivateData);
        return codeInspectors;
    }
}
