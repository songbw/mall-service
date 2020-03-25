package com.fengchao.aoyi.client.bean.dto.weipinhui.req;

import com.fengchao.aoyi.client.bean.dto.weipinhui.AoyiMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AlterationMessageRequest extends BaseRquestParam {

    private String date; // "2019-07-31 12:00:00"

    List<AoyiMessage> messages;
}
