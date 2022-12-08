var dispIns = [];
var dispCbs = [];

function Dispatcher() {
  dispIns.push(this);
  dispCbs.push({});
}

Dispatcher.prototype = {
  // 广播监听
  on(topic, cb) {
    let cbtypes = dispCbs[dispIns.indexOf(this)];
    let cbs = cbtypes[topic] = cbtypes[topic] || [];
    if (!~cbs.indexOf(cb)) { cbs.push(cb) }
  },
  // 关闭监听
  off(topic, cb) {
    let cbtypes = dispCbs[dispIns.indexOf(this)];
    let cbs = cbtypes[topic] = cbtypes[topic] || [];
    let curTypeCbIdx = cbs.indexOf(cb);
    if (~curTypeCbIdx) {
      cbs.splice(curTypeCbIdx, 1);
    }
  },
  // 发布广播
  fire(topic, ...args) {
    let cbtypes = dispCbs[dispIns.indexOf(this)];
    let cbs = cbtypes[topic] = cbtypes[topic] || [];

    for (let i = 0; i < cbs.length; i++) {
      cbs[i].apply(null, args);
    }
  }

};
module.exports = Dispatcher;