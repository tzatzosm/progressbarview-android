# ProgressBarView

Custom Progress Bar View for android, inspired by iOS's UISlider.

## Getting Started - Setup

Add the following to your repositories.

```
    maven {
        url  "http://dl.bintray.com/g20ready/ProgressBarView"
    }
```

Add the following to your dependencies.

```
    compile 'com.max.progressbarview:progressbarview:1.0.0'
```

## Usage

### Instantiation

You can either define the library in your xml resources

```xml
    <com.max.progressbarview.ProgressBarView
            android:id="@+id/progressView"
            android:layout_width="wrap_content"
            android:layout_height="50dp"/>
```

And in your activity

```
    ProgressBarView progressBarView = (ProgressBarView) findViewById(R.id.progressBarView);
```

or instantiate it via code

```
    RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(RelativeLayout
                    .LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

    ProgressBarView progressBarView = new ProgressBarView(this);
    progressBarView.setLayoutParams(progressBarParams); 
```

### Events 

This ui component provides 3 events:

    onProgressChanged(ProgressBarView progressBarView, float progress, boolean fromUser)
        Fires when progress changes either programmatically or by the user.
        
    onTouchStart(ProgressBarView progressBarView)
        Fires when user starts interacting with the view.
        
    onTouchEnd(ProgressBarView progressBarView)
        Fires when the user stops interacting with the view.

Set the listener
         
```
    progressBarView.setOnProgressChangedListener(new ProgressBarView.OnProgressChangedListener() {
        @Override
        public void onProgressChanged(ProgressBarView progressBarView, float progress, boolean fromUser) {
            Log.d(TAG, String.format("onProgressChanged %f, %b", progress, fromUser));
        }

        @Override
        public void onTouchStart(ProgressBarView progressBarView) {
            Log.d(TAG, "onTouchStart");
        }

        @Override
        public void onTouchEnd(ProgressBarView progressBarView) {
            Log.d(TAG, "onTouchEnd");
        }
    });
```

### Customization

You can customize the view either directly by xml or via code. Currently the following customization
options are provided.

    barColor
        Defines the color of the bar 
    
    primaryProgressColor
        Defines the color of the primary progress bar
    
    secondaryProgressColor
        Defines the color of the secondary progress bar
        
    thumbFillColor
        Defines the fill color ot the thumb
        
    thumbStrokeColor
        Defines the border color ot the thumb