package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.type.ControlType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record HiveControlListResponse(
        List<ControlSetting> controls
) {
    public record ControlSetting(ControlType type, Double targetValue) {}

    public static HiveControlListResponse of(List<HiveControl> controls) {
        Map<ControlType, HiveControl> controlMap = controls.stream()
                .collect(Collectors.toMap(HiveControl::getType, c -> c));

        List<ControlSetting> settings = ControlType.autoControlTypes().stream()
                .map(type -> {
                    HiveControl control = controlMap.get(type);
                    return new ControlSetting(type, control != null ? control.getTargetValue() : null);
                })
                .toList();

        return new HiveControlListResponse(settings);
    }
}
