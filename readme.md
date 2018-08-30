# Android Wifi 데이터 채집용

> 데이터는 1만개로 고정

* 2018.08.25. -> 교수님이 의뢰한 어플리케이션, 제작 시작.
* 2018.08.30. -> 종료.

---

### 동작환경

* Android 6.0 마쉬멜로우(API 23) 이상.
* Virtual Device가 아닐 것.

---

### 확인된 문제점

1. `Documents/output` 디렉토리가 없을 경우 실행 버튼 누를 때 `File` 에러가 뜰 수 있음. 이 경우 디렉토리 변경필요.
2. Wi-Fi 리스트 체크 후 나갔다가 다시 들어왔을 경우 체크가 안되는 현상

> 이 경우, 체크리스트는 정확하게 기억하고 있으나 [Adapter의 61-62 라인](https://github.com/level120/Test20180825/blob/master/app/src/main/java/kr/ac/tu/wtf/test20180825/WifiChoiceListViewAdapter.java#L61)이 생각한 대로 체크가 되지 않아 발생한 문제.

> 체크하기 위한 값은 제대로 갖고 있는데, 저 구간에서 체크가 안되는 상황이므로, 앱을 완전히 종료 후 다시 실행하면 정상 동작함.


---

### 앱 모습

| | |
|:---: | :---: |
| ![](/asset/1.png) | ![](/asset/2.png) |
| ![](/asset/3.png) | ![](/asset/4.png) |


| CSV 파일 모습 |
| :---: |
| ![](/asset/5.png) |
