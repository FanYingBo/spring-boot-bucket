package self.study.spring.cloud.common.dto.resp;

import self.study.spring.cloud.common.model.BaseEntity;

public abstract class BaseDTO<T extends BaseEntity, U extends BaseDTO> {

    protected abstract U fromModel(T e);

}
