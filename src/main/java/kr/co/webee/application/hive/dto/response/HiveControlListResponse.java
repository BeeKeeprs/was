package kr.co.webee.application.hive.dto.response;

import kr.co.webee.domain.hive.entity.HiveControl;
import kr.co.webee.domain.hive.type.ControlType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record HiveControlListResponse(
        List<AutoControlSetting> auto,
        List<ManualControlSetting> manual
) {
    public record AutoControlSetting(ControlType type, boolean enabled) {}

    public record ManualControlSetting(ControlType type, boolean isOn) {}

    public static HiveControlListResponse of(List<HiveControl> controls) {
        Map<ControlType, HiveControl> controlMap = controls.stream()
                .collect(Collectors.toMap(HiveControl::getType, c -> c));

        List<AutoControlSetting> auto = ControlType.autoControlTypes().stream()
                .map(type -> {
                    HiveControl control = controlMap.get(type);
                    return new AutoControlSetting(type, control != null && control.isAutoEnabled());
                })
                .toList();

        List<ManualControlSetting> manual = ControlType.manualControlTypes().stream()
                .map(type -> {
                    HiveControl control = controlMap.get(type);
                    return new ManualControlSetting(type, control != null && control.isManualEnabled() && control.isOn());
                })
                .toList();

        return new HiveControlListResponse(auto, manual);
    }
}
