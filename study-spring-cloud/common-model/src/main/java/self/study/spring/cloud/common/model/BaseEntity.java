package self.study.spring.cloud.common.model;


import self.study.spring.cloud.common.dto.resp.BaseDTO;

public abstract class BaseEntity<T extends BaseEntity, U extends BaseDTO>{

    protected abstract T fromDTO(U u);
}
