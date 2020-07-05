package ro.org.m2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper=true, includeFieldNames=true, doNotUseGetters = true)
public abstract class GenericModelAudited extends GenericModel {

    @NotNull
    @Column(name = "created_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected LocalDateTime createdAt;

    @NotNull
    @Column(name = "created_by")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected String createdBy;

    @Column(name = "modified_at")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected LocalDateTime modifiedAt;

    @Column(name = "modified_by")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    protected String modifiedBy;

    @PrePersist
    public void setCreatedAudit() {
        this.createdAt = LocalDateTime.now();
        this.createdBy = getCurrentUser();
    }

    @PreUpdate
    public void setModifiedAudit() {
        this.modifiedAt = LocalDateTime.now();
        this.modifiedBy = getCurrentUser();
    }

    private String getCurrentUser() {
        return "Integrate this with security";
    }

}
