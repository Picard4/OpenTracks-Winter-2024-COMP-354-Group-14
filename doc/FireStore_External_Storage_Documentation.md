# FireStore External Storage Documentation

## Limitations and roadmap

At this moment, we can perform any CRUD on any single Model, but not on collections.

Specifically, we can interact with the external storage on a per-entry basis accessed by id.

By the end of this sprint, we will have augmented the 'read' methods to deal with collections, exposing methods such as:

get all users by last name
get all runs between two dates

etc.

## Models

The FireStore database is a noSQL JSON-based storage. Therefore, all data stored in the db is in json format.

In order to preserve all the Java functionality, we added a simple interface mechanism to serialize and deserialize Java classes/models into and from json.

Example:

```
Track track = new Track();
JsonObject trackJson = track.toJSON();
Track trackCopy = JSONSerializable.fromJSON(trackJson, Track.class);
```

If you want to add serializer functionality to _any_ class, simply make it implement the JSONSerializable interface, just like in the Track model.

You don't need to do any work for the serialization, it is 100% dynamic.

## CRUD

NOTE - The ID passed to the CRUD methods is an _external_ id, and needs to be a String. It is **not** the same ID used internally by SQLite.

For any CRUD method, the class ```de.dennisguse.opentracks.data.FirestoreCRUDUtil``` is available.

This class uses the singleton pattern, so it should be used in the following way:

```
FirestoreCRUDUtil.getInstance().doSomething()
```

### Create/Update/Delete

**(Optional) Define a callback**

Because FireStore is async, we need to either use a java ``` synchronized ``` block, which would block the UI thread, or work with callbacks.

You can define your success/failure handlers by passing an ```ActionCallback``` object to the CRUD methods.

First, define the callback:

```
ActionCallback callback = new ActionCallback() {
            @Override
            public void onSuccess() {
                //Handle success
            }

            @Override
            public void onFailure() {
                //Handle failure
            }
        };
```

Then, make a call to the util and pass the data - for example:

```
FirestoreCRUDUtil.getInstance().createEntry(CRUDConstants.RUNS_TABLE, trackId, track.toJSON(), callback);
```

If you do not need a callback, simply pass ```null``` to the method.

### Read

Again, we need to define a callback to handle the retrieval of the information.

This time we'll be passing a ```ReadCallback``` object to the read method.

First, define the callback:

```
ReadCallback callback = new ReadCallback() {
            @Override
            public void onSuccess(JsonObject data) {
                Track readTrack = JSONSerializable.fromJSON(data, Track.class);

                //Do stuff with the entry
                Log.d(CRUDConstants.CRUD_OPERATION, readTrack.getDescription());
            }

            @Override
            public void onFailure() {
                //Handle failure of retrieval
            }
        };
```

Then, make a call to the util and pass the data - for example:

```
FirestoreCRUDUtil.getInstance().getEntry(CRUDConstants.RUNS_TABLE, trackId, readCallback);
```

