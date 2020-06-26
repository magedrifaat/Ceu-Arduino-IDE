# Multi-Function Shield (MFS)

`TODO`

## API

### LEDS

```
output bool OUT_13; // D1
output bool OUT_12; // D2
output bool OUT_11; // D3
output bool OUT_10; // D4
```

### BUZZER

```
output high/low OUT_03;
emit OUT_03(high);
```

### BUTTONS

```
code/await MFS_Buttons (none) -> (event (int,high/low) e) -> NEVER;
```

### DISPLAY

```
output high/low OUT_04;
output high/low OUT_07;
output high/low OUT_08;

code/await MFS_Display (none) -> NEVER;
    code/call Num (var int num) -> none;
```
