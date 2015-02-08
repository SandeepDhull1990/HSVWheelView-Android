#pragma version(1)

#pragma rs java_package_name(com.example.sandeepdhull.hsvcolorwheel)

#pragma rs_fp_relaxed

int32_t centerX;
int32_t centerY;
int32_t radius;

void root(uchar4 *v_out, uint32_t x, uint32_t y) {

float r = 0.0f;
float g = 0.0f;;
float b = 0.0f;;
int hueAngle = 0.0f;;
float saturation = 0.0f;


//Getting saturation value
    int dx = x - centerX;
    int dy = y - centerY;

    int distanceSquared = (dx*dx) + (dy*dy);
    int radiusSquared = radius * radius;

    if (distanceSquared <= radiusSquared) {
        saturation = sqrt((float)distanceSquared)/radius;
        if (saturation < 0) saturation *= -1;
    } else {
    // Point exist outside circle
        return;
    }


//Getting value of hue
    float angleInRadians = atan2((float)dy, (float)dx);
    hueAngle = (int) (angleInRadians * (180.0f /3.1415926536f));

    if (hueAngle < 0) {
        hueAngle = 360 + hueAngle;
    }


//Converting HSV to rgb values
       float h = hueAngle;
       float s = saturation;
       float v = 1.0f;

        int i;
        float f, p, q, t;
       	if( s == 0 ) {
       		// achromatic (grey)
       		r = g = b = v;
       		return;
       	}
       	h /= 60;			// sector 0 to 5
       	i = floor( h );
       	f = h - i;			// factorial part of h
       	p = v * ( 1 - s );
       	q = v * ( 1 - s * f );
       	t = v * ( 1 - s * ( 1 - f ) );
       	switch( i ) {
       		case 0:
       			r = v;
       			g = t;
       			b = p;
       			break;
       		case 1:
       			r = q;
       			g = v;
       			b = p;
       			break;
       		case 2:
       			r = p;
       			g = v;
       			b = t;
       			break;
       		case 3:
       			r = p;
       			g = q;
       			b = v;
       			break;
       		case 4:
       			r = t;
       			g = p;
       			b = v;
       			break;
       		default:		// case 5:
       			r = v;
       			g = p;
       			b = q;
       			break;
       	}

        v_out->a = 255;
        v_out->r = (int)(r * 255);
        v_out->g = (int)(g * 255);
        v_out->b = (int)(b * 255);
}