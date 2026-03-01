extends CharacterBody3D

const SPEED = 5.0
const JUMP_VELOCITY = 6.0
var gravity = ProjectSettings.get_setting("physics/3d/default_gravity")

func _physics_process(delta):
    if not is_on_floor():
        velocity.y -= gravity * delta

    var input_dir = Vector3.ZERO

    if Input.is_action_pressed("ui_right"):
        input_dir.x += 1
    if Input.is_action_pressed("ui_left"):
        input_dir.x -= 1
    if Input.is_action_pressed("ui_down"):
        input_dir.z += 1
    if Input.is_action_pressed("ui_up"):
        input_dir.z -= 1

    input_dir = input_dir.normalized()

    velocity.x = input_dir.x * SPEED
    velocity.z = input_dir.z * SPEED

    if Input.is_action_just_pressed("ui_accept") and is_on_floor():
        velocity.y = JUMP_VELOCITY

    move_and_slide()
