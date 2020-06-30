# GPIO (General-Purpose Input/Output)

GPIO pins can be controlled as either input or output pins:

- Output digital pins are writable as `high` or `low`.
- Input digital pins are readable as `high` or `low` and generate interrupts on
  changes.
- `PWM` output pins are writable with values between `0` and `255`.

Céu-Arduino currently provides the following drivers:

- Digital Output
    - [doc](out.md)
- Digital Input
    - With external interrupts
        - [doc](int.md)
    - With pin-change interrupts
        - [doc](pcint.md)
    - Difference between external and pin-change interrupts:
        - http://forum.arduino.cc/index.php?topic=221428.0
- PWM Output:
    - [doc](pwm.md)
